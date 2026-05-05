package com.dognose.platform.verification;

import com.dognose.platform.common.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dogs/{dogId}")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/verify")
    public ApiResponse<VerificationResponse> verify(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long dogId,
            @Valid @RequestBody VerificationRequest request
    ) {
        return ApiResponse.ok(verificationService.verify(userDetails.getUsername(), dogId, request));
    }

    @GetMapping("/verification-logs")
    public ApiResponse<List<VerificationResponse>> getDogVerificationLogs(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long dogId
    ) {
        return ApiResponse.ok(verificationService.getDogVerificationLogs(userDetails.getUsername(), dogId));
    }
}
