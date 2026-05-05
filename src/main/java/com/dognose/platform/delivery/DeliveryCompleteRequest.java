package com.dognose.platform.delivery;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record DeliveryCompleteRequest(
        @NotBlank
        @Size(max = 500)
        String probeImageUrl,

        @DecimalMin("0.000000")
        @DecimalMax("1.000000")
        BigDecimal threshold
) {
}
