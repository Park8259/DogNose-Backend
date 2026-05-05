package com.dognose.platform.reservation;

import com.dognose.platform.adoption.AdoptionApplication;
import com.dognose.platform.adoption.AdoptionPost;
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
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private AdoptionApplication application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private AdoptionPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;

    @Column(nullable = false)
    private LocalDateTime reservedAt;

    @Column(nullable = false, length = 255)
    private String place;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Reservation() {
    }

    public Reservation(AdoptionApplication application, LocalDateTime reservedAt, String place) {
        this.application = application;
        this.post = application.getPost();
        this.seller = application.getPost().getSeller();
        this.applicant = application.getApplicant();
        this.reservedAt = reservedAt;
        this.place = place;
        this.status = ReservationStatus.REQUESTED;
    }

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public void confirm() {
        status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        status = ReservationStatus.CANCELLED;
    }

    public void complete() {
        status = ReservationStatus.COMPLETED;
    }

    public Long getId() {
        return id;
    }

    public AdoptionApplication getApplication() {
        return application;
    }

    public AdoptionPost getPost() {
        return post;
    }

    public User getSeller() {
        return seller;
    }

    public User getApplicant() {
        return applicant;
    }

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }

    public String getPlace() {
        return place;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
