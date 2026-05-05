package com.dognose.platform.verification;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record VerificationRequest(
        @NotBlank
        @Size(max = 500)
        String probeImageUrl,

        Long postId,

        @NotNull
        VerificationType verificationType,

        @DecimalMin("0.000000")
        @DecimalMax("1.000000")
        BigDecimal threshold
) {
}
