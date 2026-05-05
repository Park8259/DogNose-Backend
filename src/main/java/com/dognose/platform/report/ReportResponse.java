package com.dognose.platform.report;

import java.time.LocalDateTime;

public record ReportResponse(
        Long id,
        Long reporterId,
        String reporterName,
        ReportTargetType targetType,
        Long targetId,
        String reason,
        String description,
        ReportStatus status,
        String adminMemo,
        LocalDateTime createdAt
) {
    public static ReportResponse from(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getReporter().getId(),
                report.getReporter().getName(),
                report.getTargetType(),
                report.getTargetId(),
                report.getReason(),
                report.getDescription(),
                report.getStatus(),
                report.getAdminMemo(),
                report.getCreatedAt()
        );
    }
}
