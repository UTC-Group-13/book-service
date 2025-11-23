package org.example.bookservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookservice.dto.response.OverdueDetailResponse;
import org.example.bookservice.dto.response.ReportResponseResponse;
import org.example.bookservice.service.BookLoanReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class BookLoanReportController {

    private final BookLoanReportService reportService;

    @GetMapping("/daily-range")
    public List<ReportResponseResponse> getReportRange(
            @RequestParam("start") String startStr,
            @RequestParam("end") String endStr) {

        LocalDate start = LocalDate.parse(startStr);
        LocalDate end = LocalDate.parse(endStr);

        return reportService.getReportsByRange(start, end)
                .stream()
                .map(r -> new ReportResponseResponse(
                        r.getReportDate(),
                        r.getTotalBorrowed(),
                        r.getTotalOverdue(),
                        r.getTotalReturned(),
                        r.getTotalFeeEstimate()
                ))
                .toList();
    }

    @GetMapping("/overdue-range")
    public List<OverdueDetailResponse> getOverdueRange(
            @RequestParam("start") String startStr,
            @RequestParam("end") String endStr) {

        LocalDate start = LocalDate.parse(startStr);
        LocalDate end = LocalDate.parse(endStr);

        return reportService.getOverdueByRange(start, end)
                .stream()
                .map(o -> new OverdueDetailResponse(
                        o.getReportDate(),
                        o.getStudentId(),
                        o.getBookId(),
                        o.getLoanId(),
                        o.getDueDate(),
                        o.getBorrowDate(),
                        o.getDaysOverdue(),
                        o.getEstimatedFee()
                ))
                .toList();
    }
}
