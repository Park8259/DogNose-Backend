package com.dognose.platform.health;

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
public class DogHealthController {

    private final HealthService healthService;

    public DogHealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @PostMapping("/health-records")
    public ApiResponse<HealthRecordResponse> createHealthRecord(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long dogId,
            @Valid @RequestBody HealthRecordCreateRequest request
    ) {
        return ApiResponse.ok(healthService.createHealthRecord(userDetails.getUsername(), dogId, request));
    }

    @GetMapping("/health-records")
    public ApiResponse<List<HealthRecordResponse>> getHealthRecords(@PathVariable Long dogId) {
        return ApiResponse.ok(healthService.getHealthRecords(dogId));
    }

    @PostMapping("/vaccinations")
    public ApiResponse<VaccinationResponse> createVaccination(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long dogId,
            @Valid @RequestBody VaccinationCreateRequest request
    ) {
        return ApiResponse.ok(healthService.createVaccination(userDetails.getUsername(), dogId, request));
    }

    @GetMapping("/vaccinations")
    public ApiResponse<List<VaccinationResponse>> getVaccinations(@PathVariable Long dogId) {
        return ApiResponse.ok(healthService.getVaccinations(dogId));
    }
}
