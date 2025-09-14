package org.example.bookservice.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookservice.utils.DateUtils;
import org.example.bookservice.constant.ErrorCode;
import org.example.bookservice.constant.Status;
import org.example.bookservice.dto.exception.BusinessException;
import org.example.bookservice.dto.request.SendEmailRequest;
import org.example.bookservice.dto.response.StudentResponse;
import org.example.bookservice.entity.Book;
import org.example.bookservice.entity.Email;
import org.example.bookservice.repository.EmailRepository;
import org.example.bookservice.service.BookService;
import org.example.bookservice.service.EmailService;
import org.example.bookservice.service.StudentService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailRepository emailRepository;
    private final BookService bookService;
    private final StudentService studentService;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendEmail(SendEmailRequest sendEmailRequest) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Email email = new Email();
        if(sendEmailRequest.getEmailId() != null){
            email.setId(sendEmailRequest.getEmailId());
        }
        email.setStudentId(sendEmailRequest.getStudentId());
        email.setBookId(sendEmailRequest.getBookId());
        try {
            StudentResponse studentResponse = studentService.getStudentById(sendEmailRequest.getStudentId());
            Book book = bookService.findById(sendEmailRequest.getBookId());
            String date = DateUtils.convertLocalDateTimeToFormat(localDateTime, "dd-MM-yyyy HH:mm:ss");
            String expirationDate = DateUtils.convertLocalDateTimeToFormat(localDateTime.plusMonths(1), "dd-MM-yyyy HH:mm:ss");

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
    public List<Email> getAllEmailWithStatus(Integer status) {
        return emailRepository.findAllByStatus(status);
    }
}