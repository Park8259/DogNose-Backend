package com.dognose.platform.admin;

import com.dognose.platform.adoption.AdoptionPostResponse;
import com.dognose.platform.adoption.AdoptionPostStatus;
import com.dognose.platform.common.ApiResponse;
import com.dognose.platform.verification.VerificationResponse;
import com.dognose.platform.verification.VerificationResult;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/adoption-posts")
    public ApiResponse<List<AdoptionPostResponse>> getAdoptionPosts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) AdoptionPostStatus status
    ) {
        return ApiResponse.ok(adminService.getAdoptionPosts(userDetails.getUsername(), status));
    }

    @PatchMapping("/adoption-posts/{postId}/status")
    public ApiResponse<AdoptionPostResponse> updateAdoptionPostStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId,
            @Valid @RequestBody AdminPostStatusUpdateRequest request
    ) {
        return ApiResponse.ok(adminService.updateAdoptionPostStatus(
                userDetails.getUsername(),
                postId,
                request
        ));
    }

    @GetMapping("/verification-logs")
    public ApiResponse<List<VerificationResponse>> getVerificationLogs(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) VerificationResult result
    ) {
        return ApiResponse.ok(adminService.getVerificationLogs(userDetails.getUsername(), result));
    }
}
