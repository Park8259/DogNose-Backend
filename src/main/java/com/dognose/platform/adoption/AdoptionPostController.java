package com.dognose.platform.adoption;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/adoption-posts")
public class AdoptionPostController {

    private final AdoptionPostService adoptionPostService;

    public AdoptionPostController(AdoptionPostService adoptionPostService) {
        this.adoptionPostService = adoptionPostService;
    }

    @PostMapping
    public ApiResponse<AdoptionPostResponse> createPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AdoptionPostCreateRequest request
    ) {
        return ApiResponse.ok(adoptionPostService.createPost(userDetails.getUsername(), request));
    }

    @GetMapping
    public ApiResponse<List<AdoptionPostResponse>> getOpenPosts(@RequestParam(required = false) String region) {
        return ApiResponse.ok(adoptionPostService.getOpenPosts(region));
    }

    @GetMapping("/{postId}")
    public ApiResponse<AdoptionPostResponse> getPost(@PathVariable Long postId) {
        return ApiResponse.ok(adoptionPostService.getPost(postId));
    }
}
