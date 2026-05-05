package com.dognose.platform.noseprint;

import com.dognose.platform.dog.Dog;
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
@Table(name = "nose_prints")
public class NosePrint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id", nullable = false)
    private Dog dog;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(length = 100)
    private String vectorPointId;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal qualityScore;

    @Column(nullable = false, length = 100)
    private String embeddingModel;

    @Column(nullable = false)
    private boolean reference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NosePrintStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected NosePrint() {
    }

    public NosePrint(
            Dog dog,
            String imageUrl,
            String vectorPointId,
            BigDecimal qualityScore,
            String embeddingModel,
            boolean reference,
            NosePrintStatus status
    ) {
        this.dog = dog;
        this.imageUrl = imageUrl;
        this.vectorPointId = vectorPointId;
        this.qualityScore = qualityScore;
        this.embeddingModel = embeddingModel;
        this.reference = reference;
        this.status = status;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVectorPointId() {
        return vectorPointId;
    }

    public BigDecimal getQualityScore() {
        return qualityScore;
    }

    public String getEmbeddingModel() {
        return embeddingModel;
    }

    public boolean isReference() {
        return reference;
    }

    public NosePrintStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
