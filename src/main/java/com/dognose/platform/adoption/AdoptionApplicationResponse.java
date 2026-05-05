package com.dognose.platform.adoption;

import java.time.LocalDateTime;

public record AdoptionApplicationResponse(
        Long id,
        Long postId,
        String postTitle,
        Long applicantId,
        String applicantName,
        String message,
        ApplicationStatus status,
        LocalDateTime createdAt
) {
    public static AdoptionApplicationResponse from(AdoptionApplication application) {
        return new AdoptionApplicationResponse(
                application.getId(),
                application.getPost().getId(),
                application.getPost().getTitle(),
                application.getApplicant().getId(),
                application.getApplicant().getName(),
                application.getMessage(),
                application.getStatus(),
                application.getCreatedAt()
        );
    }
}
