package org.example.bookservice.schedule;

import lombok.extern.slf4j.Slf4j;
import org.example.bookservice.constant.Status;
import org.example.bookservice.dto.request.SendEmailRequest;
import org.example.bookservice.entity.Email;
import org.example.bookservice.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Component
public class SendEmailScheduleProcess {

    private final EmailService emailService;

    public SendEmailScheduleProcess(EmailService emailService) {
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    void process(){
        log.info("========= Bắt đầu tiến trình thực hiện gửi email thất bại =========");
        List<Email> emails = emailService.getAllEmailWithStatus(Status.FAILURE.getValue());
        if(CollectionUtils.isEmpty(emails)){
            log.info("Không có email gửi thất bại!");
            return;
        }
        for (Email email : emails) {

            try {
                SendEmailRequest sendEmailRequest = new SendEmailRequest();
                sendEmailRequest.setBookId(email.getBookId());
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
