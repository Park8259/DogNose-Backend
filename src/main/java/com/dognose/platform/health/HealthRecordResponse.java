package com.dognose.platform.health;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record HealthRecordResponse(
        Long id,
        Long dogId,
        HealthRecordType recordType,
        String title,
        String description,
        LocalDate recordDate,
        String attachmentUrl,
        LocalDateTime createdAt
) {
    public static HealthRecordResponse from(HealthRecord record) {
        return new HealthRecordResponse(
                record.getId(),
                record.getDog().getId(),
                record.getRecordType(),
                record.getTitle(),
                record.getDescription(),
                record.getRecordDate(),
                record.getAttachmentUrl(),
                record.getCreatedAt()
        );
    }
}
