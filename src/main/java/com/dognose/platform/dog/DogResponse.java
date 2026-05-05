package com.dognose.platform.dog;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DogResponse(
        Long id,
        Long ownerId,
        String name,
        String breed,
        DogGender gender,
        LocalDate birthDate,
        String description,
        String profileImageUrl,
        boolean noseRegistered,
        DogStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static DogResponse from(Dog dog) {
        return new DogResponse(
                dog.getId(),
                dog.getOwner().getId(),
                dog.getName(),
                dog.getBreed(),
                dog.getGender(),
                dog.getBirthDate(),
                dog.getDescription(),
                dog.getProfileImageUrl(),
                dog.isNoseRegistered(),
                dog.getStatus(),
                dog.getCreatedAt(),
                dog.getUpdatedAt()
        );
    }
}
