package com.dognose.platform.report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReportCreateRequest(
        @NotNull
        ReportTargetType targetType,

        @NotNull
        Long targetId,

        @NotBlank
        @Size(max = 200)
        String reason,

        String description
) {
}
