package com.dognose.platform.verification;

import com.dognose.platform.dog.Dog;
import com.dognose.platform.noseprint.NosePrint;
import com.dognose.platform.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_logs")
public class VerificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id", nullable = false)
    private Dog dog;

    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by", nullable = false)
    private User requestedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_nose_print_id", nullable = false)
    private NosePrint referenceNosePrint;

    @Column(nullable = false, length = 500)
    private String probeImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private VerificationType verificationType;

    @Column(nullable = false, precision = 8, scale = 6)
    private BigDecimal cosineSimilarity;

    @Column(nullable = false, precision = 8, scale = 6)
    private BigDecimal euclideanDistance;

    @Column(nullable = false, precision = 8, scale = 6)
    private BigDecimal threshold;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VerificationResult result;

    @Column(nullable = false, length = 100)
    private String modelName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected VerificationLog() {
    }

    public VerificationLog(
            Dog dog,
            Long postId,
            User requestedBy,
            NosePrint referenceNosePrint,
            String probeImageUrl,
            VerificationType verificationType,
            BigDecimal cosineSimilarity,
            BigDecimal euclideanDistance,
            BigDecimal threshold,
            VerificationResult result,
            String modelName
    ) {
        this.dog = dog;
        this.postId = postId;
        this.requestedBy = requestedBy;
        this.referenceNosePrint = referenceNosePrint;
        this.probeImageUrl = probeImageUrl;
        this.verificationType = verificationType;
        this.cosineSimilarity = cosineSimilarity;
        this.euclideanDistance = euclideanDistance;
        this.threshold = threshold;
        this.result = result;
        this.modelName = modelName;
    }

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Dog getDog() {
        return dog;
    }

    public Long getPostId() {
        return postId;
    }

    public User getRequestedBy() {
        return requestedBy;
    }

    public NosePrint getReferenceNosePrint() {
        return referenceNosePrint;
    }

    public String getProbeImageUrl() {
        return probeImageUrl;
    }

    public VerificationType getVerificationType() {
        return verificationType;
    }

    public BigDecimal getCosineSimilarity() {
        return cosineSimilarity;
    }

    public BigDecimal getEuclideanDistance() {
        return euclideanDistance;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public VerificationResult getResult() {
        return result;
    }

    public String getModelName() {
        return modelName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
