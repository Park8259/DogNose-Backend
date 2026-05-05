package com.dognose.platform.dog;

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
@RequestMapping("/api/dogs")
public class DogController {

    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @PostMapping
    public ApiResponse<DogResponse> createDog(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody DogCreateRequest request
    ) {
        return ApiResponse.ok(dogService.createDog(userDetails.getUsername(), request));
    }

    @GetMapping("/me")
    public ApiResponse<List<DogResponse>> getMyDogs(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.ok(dogService.getMyDogs(userDetails.getUsername()));
    }

    @GetMapping("/{dogId}")
    public ApiResponse<DogResponse> getDog(@PathVariable Long dogId) {
        return ApiResponse.ok(dogService.getDog(dogId));
    }
}
