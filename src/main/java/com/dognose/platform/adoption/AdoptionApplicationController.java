package com.dognose.platform.adoption;

import com.dognose.platform.common.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AdoptionApplicationController {

    private final AdoptionApplicationService applicationService;

    public AdoptionApplicationController(AdoptionApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/adoption-posts/{postId}/applications")
    public ApiResponse<AdoptionApplicationResponse> apply(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId,
            @Valid @RequestBody AdoptionApplicationCreateRequest request
    ) {
        return ApiResponse.ok(applicationService.apply(userDetails.getUsername(), postId, request));
    }

    @GetMapping("/adoption-posts/{postId}/applications")
    public ApiResponse<List<AdoptionApplicationResponse>> getPostApplications(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId
    ) {
        return ApiResponse.ok(applicationService.getPostApplications(userDetails.getUsername(), postId));
    }

    @GetMapping("/applications/me")
    public ApiResponse<List<AdoptionApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ApiResponse.ok(applicationService.getMyApplications(userDetails.getUsername()));
    }

    @PatchMapping("/applications/{applicationId}/accept")
    public ApiResponse<AdoptionApplicationResponse> accept(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long applicationId
    ) {
        return ApiResponse.ok(applicationService.accept(userDetails.getUsername(), applicationId));
    }

    @PatchMapping("/applications/{applicationId}/reject")
    public ApiResponse<AdoptionApplicationResponse> reject(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long applicationId
    ) {
        return ApiResponse.ok(applicationService.reject(userDetails.getUsername(), applicationId));
    }
}
