package com.dognose.platform.adoption;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdoptionPostCreateRequest(
        @NotNull
        Long dogId,

        @NotBlank
        @Size(max = 200)
        String title,

        @NotBlank
        String content,

        @Min(0)
        int price,

        @NotBlank
        @Size(max = 100)
        String region,

        String adoptionReason,

        String contractTerms
) {
}
