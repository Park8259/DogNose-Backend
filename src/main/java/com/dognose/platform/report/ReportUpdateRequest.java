package com.dognose.platform.report;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReportUpdateRequest(
        @NotNull
        ReportStatus status,

        @Size(max = 500)
        String adminMemo
) {
}
