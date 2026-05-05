package com.dognose.platform.health;

import com.dognose.platform.dog.Dog;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "vaccinations")
public class Vaccination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id", nullable = false)
    private Dog dog;

    @Column(nullable = false, length = 100)
    private String vaccineName;

    @Column(nullable = false)
    private LocalDate vaccinatedAt;

    private LocalDate nextDueAt;

    @Column(length = 100)
    private String hospitalName;

    @Column(length = 500)
    private String attachmentUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Vaccination() {
    }

    public Vaccination(
            Dog dog,
            String vaccineName,
            LocalDate vaccinatedAt,
            LocalDate nextDueAt,
            String hospitalName,
            String attachmentUrl
    ) {
        this.dog = dog;
        this.vaccineName = vaccineName;
        this.vaccinatedAt = vaccinatedAt;
        this.nextDueAt = nextDueAt;
        this.hospitalName = hospitalName;
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

    public String getVaccineName() {
        return vaccineName;
    }

    public LocalDate getVaccinatedAt() {
        return vaccinatedAt;
    }

    public LocalDate getNextDueAt() {
        return nextDueAt;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
