package com.dognose.platform.verification;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@ConditionalOnProperty(name = "app.ai.enabled", havingValue = "true")
public class AiNosePrintMatcher implements NosePrintMatcher {

    private final RestClient restClient;

    public AiNosePrintMatcher(@Value("${app.ai.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public NosePrintMatchResult verify(String referenceImageUrl, String probeImageUrl, BigDecimal threshold) {
        AiVerifyResponse response = restClient.post()
                .uri("/ai/nose-prints/verify")
                .body(new AiVerifyRequest(referenceImageUrl, probeImageUrl, threshold))
                .retrieve()
                .body(AiVerifyResponse.class);

        if (response == null) {
            throw new IllegalStateException("AI 서버 응답이 비어 있습니다.");
        }
        if (response.result() == null || response.cosineSimilarity() == null || response.euclideanDistance() == null) {
            throw new IllegalStateException("AI 서버 응답 형식이 올바르지 않습니다.");
        }

        return new NosePrintMatchResult(
                response.cosineSimilarity(),
                response.euclideanDistance(),
                response.threshold() == null ? threshold : response.threshold(),
                response.result(),
                response.modelName() == null || response.modelName().isBlank() ? "s101_224" : response.modelName()
        );
    }

    private record AiVerifyRequest(
            String referenceImageUrl,
            String probeImageUrl,
            BigDecimal threshold
    ) {
    }

    private record AiVerifyResponse(
            VerificationResult result,
            BigDecimal cosineSimilarity,
            BigDecimal euclideanDistance,
            BigDecimal threshold,
            String modelName
    ) {
    }
}
