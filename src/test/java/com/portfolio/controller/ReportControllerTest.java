package com.portfolio.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.portfolio.dto.PortfolioReportResponse;
import com.portfolio.enums.ProjectStatus;
import com.portfolio.service.ReportService;

@WebMvcTest(ReportController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ReportController - Endpoints de relatórios")
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    @DisplayName("GET /api/reports/portfolio deve retornar relatório")
    void getPortfolioReport_shouldReturnReport() throws Exception {
        PortfolioReportResponse response = new PortfolioReportResponse(
                Map.of(ProjectStatus.EM_ANALISE, 1L),
                Map.of(ProjectStatus.EM_ANALISE, new BigDecimal("50000")),
                30.0,
                5L
        );
        when(reportService.generatePortfolioReport()).thenReturn(response);

        mockMvc.perform(get("/api/reports/portfolio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageDurationOfClosedProjects").value(30.0))
                .andExpect(jsonPath("$.totalUniqueAllocatedMembers").value(5));
    }
}
