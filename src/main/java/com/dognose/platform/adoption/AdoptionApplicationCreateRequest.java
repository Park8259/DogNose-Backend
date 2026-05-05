package com.dognose.platform.adoption;

import jakarta.validation.constraints.NotBlank;

public record AdoptionApplicationCreateRequest(
        @NotBlank
        String message
) {
}
