package com.dognose.platform.dog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record DogCreateRequest(
        @NotBlank
        @Size(max = 100)
        String name,

        @Size(max = 100)
        String breed,

        @NotNull
        DogGender gender,

        LocalDate birthDate,

        String description,

        @Size(max = 500)
        String profileImageUrl
) {
}
