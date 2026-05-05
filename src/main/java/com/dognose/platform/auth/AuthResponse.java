package com.dognose.platform.auth;

import com.dognose.platform.user.UserResponse;

public record AuthResponse(
        String accessToken,
        String tokenType,
        UserResponse user
) {
}
