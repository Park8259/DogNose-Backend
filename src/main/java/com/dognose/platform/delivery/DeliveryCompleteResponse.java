package com.dognose.platform.delivery;

import com.dognose.platform.reservation.ReservationResponse;
import com.dognose.platform.verification.VerificationResponse;

public record DeliveryCompleteResponse(
        ReservationResponse reservation,
        VerificationResponse verification
) {
}
