package com.dognose.platform.user;

public record UserResponse(
        Long id,
        String email,
        String name,
        String phone,
        UserRole role,
        UserStatus status
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                user.getRole(),
                user.getStatus()
        );
    }
}
