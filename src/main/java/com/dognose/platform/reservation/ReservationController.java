package com.dognose.platform.reservation;

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
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ApiResponse<ReservationResponse> createReservation(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ReservationCreateRequest request
    ) {
        return ApiResponse.ok(reservationService.createReservation(userDetails.getUsername(), request));
    }

    @GetMapping("/me")
    public ApiResponse<List<ReservationResponse>> getMyReservations(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ApiResponse.ok(reservationService.getMyReservations(userDetails.getUsername()));
    }

    @PatchMapping("/{reservationId}/confirm")
    public ApiResponse<ReservationResponse> confirm(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long reservationId
    ) {
        return ApiResponse.ok(reservationService.confirm(userDetails.getUsername(), reservationId));
    }

    @PatchMapping("/{reservationId}/cancel")
    public ApiResponse<ReservationResponse> cancel(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long reservationId
    ) {
        return ApiResponse.ok(reservationService.cancel(userDetails.getUsername(), reservationId));
    }

    @PatchMapping("/{reservationId}/complete")
    public ApiResponse<ReservationResponse> complete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long reservationId
    ) {
        return ApiResponse.ok(reservationService.complete(userDetails.getUsername(), reservationId));
    }
}
