package com.dognose.platform.verification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.ai.enabled", havingValue = "false", matchIfMissing = true)
public class MockNosePrintMatcher implements NosePrintMatcher {

    @Override
    public NosePrintMatchResult verify(String referenceImageUrl, String probeImageUrl, BigDecimal threshold) {
        MatchScores scores = compare(referenceImageUrl, probeImageUrl);
        VerificationResult result = scores.cosineSimilarity().compareTo(threshold) >= 0
                ? VerificationResult.MATCH
                : VerificationResult.NON_MATCH;
        return new NosePrintMatchResult(
                scores.cosineSimilarity(),
                scores.euclideanDistance(),
                threshold,
                result,
                "mock-s101_224"
        );
    }

    private MatchScores compare(String referenceImageUrl, String probeImageUrl) {
        if (referenceImageUrl.equals(probeImageUrl)) {
            return new MatchScores(decimal("0.950000"), decimal("0.316228"));
        }

        int hash = Math.abs((referenceImageUrl + "|" + probeImageUrl).hashCode());
        BigDecimal cosine = BigDecimal.valueOf(0.55 + (hash % 4000) / 10000.0)
                .setScale(6, RoundingMode.HALF_UP);
        BigDecimal euclidean = BigDecimal.valueOf(Math.sqrt(Math.max(0.0, 2.0 - 2.0 * cosine.doubleValue())))
                .setScale(6, RoundingMode.HALF_UP);
        return new MatchScores(cosine, euclidean);
    }

    private BigDecimal decimal(String value) {
        return new BigDecimal(value);
    }

    private record MatchScores(
            BigDecimal cosineSimilarity,
            BigDecimal euclideanDistance
    ) {
    }
}
