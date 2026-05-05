package com.dognose.platform.admin;

import com.dognose.platform.adoption.AdoptionPostStatus;
import jakarta.validation.constraints.NotNull;

public record AdminPostStatusUpdateRequest(
        @NotNull
        AdoptionPostStatus status
) {
}
