package com.dognose.platform.auth;

import com.dognose.platform.security.JwtTokenProvider;
import com.dognose.platform.user.User;
import com.dognose.platform.user.UserRepository;
import com.dognose.platform.user.UserResponse;
import com.dognose.platform.user.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = new User(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.name(),
                request.phone(),
                request.role()
        );

        User savedUser = userRepository.save(user);
        String token = jwtTokenProvider.createToken(savedUser.getEmail(), savedUser.getRole().name());
        return new AuthResponse(token, "Bearer", UserResponse.from(savedUser));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("사용할 수 없는 계정입니다.");
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, "Bearer", UserResponse.from(user));
    }
}
