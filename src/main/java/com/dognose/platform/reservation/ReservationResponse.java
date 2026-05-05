package com.dognose.platform.reservation;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long applicationId,
        Long postId,
        String postTitle,
        Long sellerId,
        String sellerName,
        Long applicantId,
        String applicantName,
        LocalDateTime reservedAt,
        String place,
        ReservationStatus status,
        LocalDateTime createdAt
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getApplication().getId(),
                reservation.getPost().getId(),
                reservation.getPost().getTitle(),
                reservation.getSeller().getId(),
                reservation.getSeller().getName(),
                reservation.getApplicant().getId(),
                reservation.getApplicant().getName(),
                reservation.getReservedAt(),
                reservation.getPlace(),
                reservation.getStatus(),
                reservation.getCreatedAt()
        );
    }
}
