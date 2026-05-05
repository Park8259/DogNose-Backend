package com.dognose.platform.adoption;

import com.dognose.platform.dog.Dog;
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
import java.time.LocalDateTime;

@Entity
@Table(name = "adoption_posts")
public class AdoptionPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id", nullable = false)
    private Dog dog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, length = 100)
    private String region;

    @Column(columnDefinition = "TEXT")
    private String adoptionReason;

    @Column(columnDefinition = "TEXT")
    private String contractTerms;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AdoptionPostStatus status;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected AdoptionPost() {
    }

    public AdoptionPost(
            Dog dog,
            User seller,
            String title,
            String content,
            int price,
            String region,
            String adoptionReason,
            String contractTerms
    ) {
        this.dog = dog;
        this.seller = seller;
        this.title = title;
        this.content = content;
        this.price = price;
        this.region = region;
        this.adoptionReason = adoptionReason;
        this.contractTerms = contractTerms;
        this.status = AdoptionPostStatus.OPEN;
        this.viewCount = 0;
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

    public void increaseViewCount() {
        viewCount++;
    }

    public void markDelivered() {
        this.status = AdoptionPostStatus.DELIVERED;
    }

    public void changeStatus(AdoptionPostStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Dog getDog() {
        return dog;
    }

    public User getSeller() {
        return seller;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getPrice() {
        return price;
    }

    public String getRegion() {
        return region;
    }

    public String getAdoptionReason() {
        return adoptionReason;
    }

    public String getContractTerms() {
        return contractTerms;
    }

    public AdoptionPostStatus getStatus() {
        return status;
    }

    public int getViewCount() {
        return viewCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
