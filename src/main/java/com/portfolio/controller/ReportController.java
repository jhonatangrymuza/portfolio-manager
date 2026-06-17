package com.portfolio.controller;

import com.portfolio.dto.PortfolioReportResponse;
import com.portfolio.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/portfolio")
    public PortfolioReportResponse getPortfolioReport() {
        return reportService.generatePortfolioReport();
    }
}
