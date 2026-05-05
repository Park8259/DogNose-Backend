package com.dognose.platform.health;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record VaccinationCreateRequest(
        @NotBlank
        @Size(max = 100)
        String vaccineName,

        @NotNull
        LocalDate vaccinatedAt,

        LocalDate nextDueAt,

        @Size(max = 100)
        String hospitalName,

        @Size(max = 500)
        String attachmentUrl
) {
}
