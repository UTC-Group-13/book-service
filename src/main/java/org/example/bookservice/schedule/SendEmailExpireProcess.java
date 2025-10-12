package org.example.bookservice.schedule;

import lombok.extern.slf4j.Slf4j;
import org.example.bookservice.constant.Status;
import org.example.bookservice.dto.request.SendEmailRequest;
import org.example.bookservice.entity.BookLoan;
import org.example.bookservice.entity.Email;
import org.example.bookservice.service.BookLoanService;
import org.example.bookservice.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class SendEmailExpireProcess {

    private final EmailService emailService;
    private final BookLoanService bookLoanService;

    public SendEmailExpireProcess(EmailService emailService, BookLoanService bookLoanService) {
        this.emailService = emailService;
        this.bookLoanService = bookLoanService;
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    void process() {
        log.info("========= Gui email het han =========");
        LocalDate localDate = LocalDate.now();
        List<BookLoan> bookLoans = bookLoanService.findExpiredLoans(localDate, List.of(Status.LATE.getValue()));
        if (CollectionUtils.isEmpty(bookLoans)) {
            log.info("Khong co phieu muon qua han!");
            return;
        }
        try {
            SendEmailRequest sendEmailRequest = new SendEmailRequest();
            sendEmailRequest.setBookLoans(bookLoans);
            emailService.sendEmailExpire(sendEmailRequest);
        } catch (Exception e) {
            log.error("Lỗi khi gửi lại email quá han lỗi: {}", e.getMessage(), e);
        }

        log.info("========= Ket thuc gui email het han =========");

    }

}
