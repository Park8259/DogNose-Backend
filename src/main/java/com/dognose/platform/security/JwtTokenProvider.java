package com.dognose.platform.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final ObjectMapper objectMapper;
    private final String secret;
    private final long expirationMs;

    public JwtTokenProvider(
            ObjectMapper objectMapper,
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs
    ) {
        this.objectMapper = objectMapper;
        this.secret = secret;
        this.expirationMs = expirationMs;
    }

    public String createToken(String email, String role) {
        try {
            Instant now = Instant.now();

            Map<String, Object> header = new LinkedHashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("sub", email);
            payload.put("role", role);
            payload.put("iat", now.getEpochSecond());
            payload.put("exp", now.plusMillis(expirationMs).getEpochSecond());

            String encodedHeader = base64UrlEncode(objectMapper.writeValueAsBytes(header));
            String encodedPayload = base64UrlEncode(objectMapper.writeValueAsBytes(payload));
            String unsignedToken = encodedHeader + "." + encodedPayload;
            String signature = sign(unsignedToken);

            return unsignedToken + "." + signature;
        } catch (Exception exception) {
            throw new IllegalStateException("JWT 토큰 생성에 실패했습니다.", exception);
        }
    }

    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }

            String unsignedToken = parts[0] + "." + parts[1];
            if (!sign(unsignedToken).equals(parts[2])) {
                return false;
            }

            Map<String, Object> payload = readPayload(parts[1]);
            Number exp = (Number) payload.get("exp");
            return exp != null && exp.longValue() > Instant.now().getEpochSecond();
        } catch (Exception exception) {
            return false;
        }
    }

    public String getEmail(String token) {
        Map<String, Object> payload = readPayload(token.split("\\.")[1]);
        return (String) payload.get("sub");
    }

    public String getRole(String token) {
        Map<String, Object> payload = readPayload(token.split("\\.")[1]);
        return (String) payload.get("role");
    }

    private Map<String, Object> readPayload(String encodedPayload) {
        try {
            byte[] decoded = Base64.getUrlDecoder().decode(encodedPayload);
            return objectMapper.readValue(decoded, Map.class);
        } catch (Exception exception) {
            throw new IllegalArgumentException("JWT 토큰을 읽을 수 없습니다.", exception);
        }
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            return base64UrlEncode(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("JWT 서명에 실패했습니다.", exception);
        }
    }

    private String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
