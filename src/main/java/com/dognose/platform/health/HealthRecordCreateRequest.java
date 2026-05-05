package com.dognose.platform.health;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record HealthRecordCreateRequest(
        @NotNull
        HealthRecordType recordType,

        @NotBlank
        @Size(max = 200)
        String title,

        String description,

        @NotNull
        LocalDate recordDate,

        @Size(max = 500)
        String attachmentUrl
) {
}
