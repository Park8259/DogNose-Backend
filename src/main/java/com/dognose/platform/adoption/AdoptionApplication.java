package com.dognose.platform.adoption;

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
import java.time.LocalDateTime;

@Entity
@Table(name = "adoption_applications")
public class AdoptionApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private AdoptionPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected AdoptionApplication() {
    }

    public AdoptionApplication(AdoptionPost post, User applicant, String message) {
        this.post = post;
        this.applicant = applicant;
        this.message = message;
        this.status = ApplicationStatus.PENDING;
    }

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public void accept() {
        status = ApplicationStatus.ACCEPTED;
    }

    public void reject() {
        status = ApplicationStatus.REJECTED;
    }

    public Long getId() {
        return id;
    }

    public AdoptionPost getPost() {
        return post;
    }

    public User getApplicant() {
        return applicant;
    }

    public String getMessage() {
        return message;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
