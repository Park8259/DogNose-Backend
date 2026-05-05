package com.dognose.platform.report;

import com.dognose.platform.user.User;
import com.dognose.platform.user.UserRepository;
import com.dognose.platform.user.UserRole;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ReportResponse createReport(String reporterEmail, ReportCreateRequest request) {
        User reporter = findUser(reporterEmail);
        Report report = new Report(
                reporter,
                request.targetType(),
                request.targetId(),
                request.reason(),
                request.description()
        );
        return ReportResponse.from(reportRepository.save(report));
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getMyReports(String reporterEmail) {
        User reporter = findUser(reporterEmail);
        return reportRepository.findAllByReporterOrderByCreatedAtDesc(reporter)
                .stream()
                .map(ReportResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getAdminReports(String adminEmail, ReportStatus status) {
        validateAdmin(adminEmail);
        List<Report> reports = status == null
                ? reportRepository.findAllByOrderByCreatedAtDesc()
                : reportRepository.findAllByStatusOrderByCreatedAtDesc(status);
        return reports.stream()
                .map(ReportResponse::from)
                .toList();
    }

    @Transactional
    public ReportResponse updateReport(String adminEmail, Long reportId, ReportUpdateRequest request) {
        validateAdmin(adminEmail);
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고를 찾을 수 없습니다."));
        report.updateStatus(request.status(), request.adminMemo());
        return ReportResponse.from(report);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private void validateAdmin(String email) {
        User user = findUser(email);
        if (user.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("관리자만 처리할 수 있습니다.");
        }
    }
}
