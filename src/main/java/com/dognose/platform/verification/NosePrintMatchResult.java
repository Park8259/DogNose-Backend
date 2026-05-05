package com.dognose.platform.verification;

import java.math.BigDecimal;

public record NosePrintMatchResult(
        BigDecimal cosineSimilarity,
        BigDecimal euclideanDistance,
        BigDecimal threshold,
        VerificationResult result,
        String modelName
) {
}
