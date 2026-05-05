package com.dognose.platform.health;

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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_records")
public class HealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id", nullable = false)
    private Dog dog;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private HealthRecordType recordType;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate recordDate;

    @Column(length = 500)
    private String attachmentUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected HealthRecord() {
    }

    public HealthRecord(
            Dog dog,
            HealthRecordType recordType,
            String title,
            String description,
            LocalDate recordDate,
            String attachmentUrl
    ) {
        this.dog = dog;
        this.recordType = recordType;
        this.title = title;
        this.description = description;
        this.recordDate = recordDate;
        this.attachmentUrl = attachmentUrl;
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

    public HealthRecordType getRecordType() {
        return recordType;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
