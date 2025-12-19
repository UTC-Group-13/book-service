package org.example.bookservice.schedule;

import lombok.extern.slf4j.Slf4j;
import org.example.bookservice.constant.Status;
import org.example.bookservice.dto.request.SendEmailRequest;
import org.example.bookservice.entity.Book;
import org.example.bookservice.entity.Email;
import org.example.bookservice.service.BookService;
import org.example.bookservice.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SendEmailProcess {

    private final EmailService emailService;
    private final BookService bookService;

    public SendEmailProcess(EmailService emailService, BookService bookService) {
        this.emailService = emailService;
        this.bookService = bookService;
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    void process(){
        log.info("========= Bat dau tien trinh gui email qua han =========");
        List<Email> emails = emailService.getAllEmailWithStatus(Status.FAILURE.getValue());
        if(CollectionUtils.isEmpty(emails)){
            log.info("Khong co email gui that bai!");
            return;
        }

        for (Email email : emails) {
            try {
                SendEmailRequest sendEmailRequest = new SendEmailRequest();
                // 1 phan tu thoi nen lay tai 0
                sendEmailRequest.setBookId(Long.parseLong(email.getBookIds().split(",")[0]));
                sendEmailRequest.setStudentId(email.getStudentId());
                sendEmailRequest.setEmailId(email.getId());
                emailService.sendEmail(sendEmailRequest);
            } catch (Exception e) {
                log.error("Lỗi khi gửi lại email có id: {}, lỗi: {}", email.getId(), e.getMessage(), e);
            }
        }
        log.info("========= Kết thúc tiến trình thực hiện gửi email thất bại =========");

    }

}
