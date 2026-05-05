package com.dognose.platform.dog;

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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dogs")
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String breed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DogGender gender;

    private LocalDate birthDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String profileImageUrl;

    @Column(nullable = false)
    private boolean noseRegistered;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DogStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected Dog() {
    }

    public Dog(
            User owner,
            String name,
            String breed,
            DogGender gender,
            LocalDate birthDate,
            String description,
            String profileImageUrl
    ) {
        this.owner = owner;
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.birthDate = birthDate;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
        this.noseRegistered = false;
        this.status = DogStatus.ACTIVE;
    }

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void markNoseRegistered() {
        this.noseRegistered = true;
    }

    public void markAdopted() {
        this.status = DogStatus.ADOPTED;
    }

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public DogGender getGender() {
        return gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getDescription() {
        return description;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public boolean isNoseRegistered() {
        return noseRegistered;
    }

    public DogStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
