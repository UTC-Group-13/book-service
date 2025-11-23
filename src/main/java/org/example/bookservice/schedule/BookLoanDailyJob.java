package org.example.bookservice.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookservice.service.impl.BookLoanReportServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookLoanDailyJob {
    private final BookLoanReportServiceImpl reportService;

    // Chạy 01:00 sáng mỗi ngày
    @Scheduled(cron = "0 * * * * ?")
//    @Scheduled(cron = "0 0 1 * * ?")
    public void runDailyReportJob() {
        log.info("==== BẮT ĐẦU JOB THỐNG KÊ MƯỢN TRẢ ====");
        reportService.generateDailyReport();
        log.info("==== KẾT THÚC JOB ====");
    }
}
