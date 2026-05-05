package com.dognose.platform.adoption;

import com.dognose.platform.dog.DogGender;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AdoptionPostResponse(
        Long id,
        Long dogId,
        String dogName,
        String breed,
        DogGender gender,
        LocalDate birthDate,
        String profileImageUrl,
        boolean noseRegistered,
        Long sellerId,
        String sellerName,
        String title,
        String content,
        int price,
        String region,
        String adoptionReason,
        String contractTerms,
        AdoptionPostStatus status,
        int viewCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static AdoptionPostResponse from(AdoptionPost post) {
        return new AdoptionPostResponse(
                post.getId(),
                post.getDog().getId(),
                post.getDog().getName(),
                post.getDog().getBreed(),
                post.getDog().getGender(),
                post.getDog().getBirthDate(),
                post.getDog().getProfileImageUrl(),
                post.getDog().isNoseRegistered(),
                post.getSeller().getId(),
                post.getSeller().getName(),
                post.getTitle(),
                post.getContent(),
                post.getPrice(),
                post.getRegion(),
                post.getAdoptionReason(),
                post.getContractTerms(),
                post.getStatus(),
                post.getViewCount(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
