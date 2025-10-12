package org.example.bookservice.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookservice.constant.ErrorCode;
import org.example.bookservice.constant.Status;
import org.example.bookservice.dto.exception.BusinessException;
import org.example.bookservice.dto.request.SendEmailRequest;
import org.example.bookservice.dto.response.BookResponse;
import org.example.bookservice.dto.response.StudentResponse;
import org.example.bookservice.entity.Book;
import org.example.bookservice.entity.BookLoan;
import org.example.bookservice.entity.Email;
import org.example.bookservice.repository.EmailRepository;
import org.example.bookservice.service.BookLoanService;
import org.example.bookservice.service.BookService;
import org.example.bookservice.service.EmailService;
import org.example.bookservice.service.StudentService;
import org.example.bookservice.utils.DateUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailRepository emailRepository;
    private final BookService bookService;
    private final BookLoanService bookLoanService;
    private final StudentService studentService;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendEmail(SendEmailRequest sendEmailRequest) {
        LocalDate localDate = LocalDate.now();
        Email email = new Email();
        if (sendEmailRequest.getEmailId() != null) {
            email.setId(sendEmailRequest.getEmailId());
        }
        BookResponse book = bookService.getBookById(sendEmailRequest.getBookId());
        email.setStudentId(sendEmailRequest.getStudentId());
        email.setBookIds(String.valueOf(book.getId()));
        try {
            StudentResponse studentResponse = studentService.getStudentById(sendEmailRequest.getStudentId());
            String date = DateUtils.convertLocalDateToFormat(localDate, "dd-MM-yyyy");
            String expirationDate = sendEmailRequest.getDueDate() != null ? DateUtils.convertLocalDateToFormat(sendEmailRequest.getDueDate(), "dd-MM-yyyy") : DateUtils.convertLocalDateToFormat(localDate.plusMonths(1), "dd-MM-yyyy");

            Map<String, Object> variables = new HashMap<>();
            variables.put("studentName", studentResponse.getFullName());
            variables.put("bookTitle", book.getTitle());
            variables.put("date", date);
            variables.put("expirationDate", expirationDate);
            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process("email-template", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(studentResponse.getEmail());
            helper.setSubject("Thông báo mượn sách thành công");
            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);

            email.setStatus(Status.SUCCESS.getValue());
            emailRepository.save(email);
        } catch (Exception e) {
            log.error("Error when send email: {}", e.getMessage(), e);
            email.setStatus(Status.FAILURE.getValue());
            emailRepository.save(email);
            throw new BusinessException(ErrorCode.SEND_EMAIL_ERROR.getCode(), ErrorCode.SEND_EMAIL_ERROR.getMessage());
        }
    }

    @Override
    public void sendEmailExpire(SendEmailRequest sendEmailRequest) {
        Email email = new Email();
        if (sendEmailRequest.getEmailId() != null) {
            email.setId(sendEmailRequest.getEmailId());
        }
        List<BookLoan> bookLoans = sendEmailRequest.getBookLoans();
        if (CollectionUtils.isEmpty(bookLoans)) {
            log.warn("Không có phiếu mượn nào để gửi email quá hạn.");
            return;
        }
        List<Integer> lstBookIds = bookLoans.stream().map(BookLoan::getBookId).toList();
        List<Book> lstBook = bookService.findAllByIds(lstBookIds);
        try {
            StudentResponse studentResponse = new StudentResponse();
            Map<Integer, List<BookLoan>> loansByStudent = bookLoans.stream()
                    .filter(loan -> loan.getReturnDate() == null)
                    .collect(Collectors.groupingBy(BookLoan::getStudentId));
            List<BookLoan> lstBkLoan = new ArrayList<>();
            for (Map.Entry<Integer, List<BookLoan>> entry : loansByStudent.entrySet()) {
                Integer studentId = entry.getKey();
                List<BookLoan> loansOfStudent = entry.getValue();
                studentResponse = studentService.getStudentById(studentId);

                // Danh sách hiển thị trong email
                List<Map<String, Object>> loansForTemplate = new ArrayList<>();
                for (BookLoan loan : loansOfStudent) {
                    LocalDate today = LocalDate.now();
                    if(today.equals(loan.getLastEmailSentDate())){
                        log.info("Đã gửi email nhắc trả sách trong hôm nay cho sinh viên: {}, bỏ qua...", studentResponse.getFullName());
                        continue;
                    }
                    Book book = lstBook.stream()
                            .filter(b -> b.getId().equals(loan.getBookId()))
                            .findFirst()
                            .orElse(null);
                    if (book == null) continue;

                    long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now());
                    if (daysLate <= 0) continue;

                    Map<String, Object> row = new HashMap<>();
                    row.put("bookTitle", book.getTitle());
                    row.put("dueDate", loan.getDueDate());
                    row.put("daysLate", daysLate);
                    row.put("fine", loan.getFee());
                    loansForTemplate.add(row);
                    loan.setLastEmailSentDate(today);
                    lstBkLoan.add(loan);
                }

                if (loansForTemplate.isEmpty()) {
                    log.info("Không có sách quá hạn để gửi email cho sinh viên {}", studentResponse.getFullName());
                    continue;
                }

                // Tổng tiền phạt của sinh viên
                BigDecimal totalFine = loansOfStudent.stream()
                        .map(BookLoan::getFee)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Biến truyền vào Thymeleaf
                Map<String, Object> variables = new HashMap<>();
                variables.put("member", Map.of("name", studentResponse.getFullName()));
                variables.put("loans", loansForTemplate);
                variables.put("fineAmount", totalFine);
                variables.put("today", LocalDate.now());
                variables.put("library", Map.of(
                        "email", "library@example.com",
                        "phone", "0123456789"
                ));

                Context context = new Context();
                context.setVariables(variables);
                String htmlContent = templateEngine.process("email-expire-template", context);

                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setTo(studentResponse.getEmail());
                helper.setSubject("Thông báo sách mượn đã quá hạn");
                helper.setText(htmlContent, true);
                javaMailSender.send(mimeMessage);

                log.info("Đã gửi email nhắc trả sách cho sinh viên: {}", studentResponse.getFullName());
            }
            // Lưu log email gửi thành công
            email.setStatus(Status.SUCCESS.getValue());
            email.setStudentId(studentResponse.getId());
            StudentResponse finalStudentResponse = studentResponse;
            email.setBookIds(bookLoans.stream()
                    .filter(b -> b.getStudentId().equals(finalStudentResponse.getId()))
                    .map(BookLoan::getBookId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")));
            emailRepository.save(email);
            bookLoanService.saveAll(lstBkLoan);
            log.info("Đã gửi email quá hạn thành công cho sinh viên {}", studentResponse.getFullName());
        } catch (Exception e) {
            log.error("Error when send email: {}", e.getMessage(), e);
            email.setStatus(Status.FAILURE.getValue());
            emailRepository.save(email);
            throw new BusinessException(ErrorCode.SEND_EMAIL_ERROR.getCode(), ErrorCode.SEND_EMAIL_ERROR.getMessage());
        }
    }

    @Override
    public List<Email> getAllEmailWithStatus(String status) {
        return emailRepository.findAllByStatus(status);
    }
}