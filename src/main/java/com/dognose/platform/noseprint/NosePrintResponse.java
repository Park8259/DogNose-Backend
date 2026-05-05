package com.dognose.platform.noseprint;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record NosePrintResponse(
        Long id,
        Long dogId,
        String imageUrl,
        String vectorPointId,
        BigDecimal qualityScore,
        String embeddingModel,
        boolean reference,
        NosePrintStatus status,
        LocalDateTime createdAt
) {
    public static NosePrintResponse from(NosePrint nosePrint) {
        return new NosePrintResponse(
                nosePrint.getId(),
                nosePrint.getDog().getId(),
                nosePrint.getImageUrl(),
                nosePrint.getVectorPointId(),
                nosePrint.getQualityScore(),
                nosePrint.getEmbeddingModel(),
                nosePrint.isReference(),
                nosePrint.getStatus(),
                nosePrint.getCreatedAt()
        );
    }
}
