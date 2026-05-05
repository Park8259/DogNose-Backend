package com.dognose.platform.reservation;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record ReservationCreateRequest(
        @NotNull
        Long applicationId,

        @NotNull
        @Future
        LocalDateTime reservedAt,

        @NotBlank
        @Size(max = 255)
        String place
) {
}
