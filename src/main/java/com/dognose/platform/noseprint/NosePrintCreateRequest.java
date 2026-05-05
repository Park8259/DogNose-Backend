package com.dognose.platform.noseprint;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record NosePrintCreateRequest(
        @NotBlank
        @Size(max = 500)
        String imageUrl,

        @NotNull
        @DecimalMin("0.00")
        @DecimalMax("100.00")
        BigDecimal qualityScore,

        @Size(max = 100)
        String vectorPointId,

        @Size(max = 100)
        String embeddingModel,

        Boolean reference
) {
}
