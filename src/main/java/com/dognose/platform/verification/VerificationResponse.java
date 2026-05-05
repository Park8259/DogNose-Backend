package com.dognose.platform.verification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VerificationResponse(
        Long id,
        Long dogId,
        Long postId,
        Long requestedBy,
        Long referenceNosePrintId,
        String referenceImageUrl,
        String probeImageUrl,
        VerificationType verificationType,
        BigDecimal cosineSimilarity,
        BigDecimal euclideanDistance,
        BigDecimal threshold,
        VerificationResult result,
        String modelName,
        LocalDateTime createdAt
) {
    public static VerificationResponse from(VerificationLog log) {
        return new VerificationResponse(
                log.getId(),
                log.getDog().getId(),
                log.getPostId(),
                log.getRequestedBy().getId(),
                log.getReferenceNosePrint().getId(),
                log.getReferenceNosePrint().getImageUrl(),
                log.getProbeImageUrl(),
                log.getVerificationType(),
                log.getCosineSimilarity(),
                log.getEuclideanDistance(),
                log.getThreshold(),
                log.getResult(),
                log.getModelName(),
                log.getCreatedAt()
        );
    }
}
