package com.dognose.platform.report;

import com.dognose.platform.common.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/reports")
    public ApiResponse<ReportResponse> createReport(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ReportCreateRequest request
    ) {
        return ApiResponse.ok(reportService.createReport(userDetails.getUsername(), request));
    }

    @GetMapping("/reports/me")
    public ApiResponse<List<ReportResponse>> getMyReports(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.ok(reportService.getMyReports(userDetails.getUsername()));
    }

    @GetMapping("/admin/reports")
    public ApiResponse<List<ReportResponse>> getAdminReports(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) ReportStatus status
    ) {
        return ApiResponse.ok(reportService.getAdminReports(userDetails.getUsername(), status));
    }

    @PatchMapping("/admin/reports/{reportId}")
    public ApiResponse<ReportResponse> updateReport(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long reportId,
            @Valid @RequestBody ReportUpdateRequest request
    ) {
        return ApiResponse.ok(reportService.updateReport(userDetails.getUsername(), reportId, request));
    }
}
