package com.dognose.platform.delivery;

import com.dognose.platform.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations/{reservationId}/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("/complete")
    public ApiResponse<DeliveryCompleteResponse> completeDelivery(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long reservationId,
            @Valid @RequestBody DeliveryCompleteRequest request
    ) {
        return ApiResponse.ok(deliveryService.completeDelivery(
                userDetails.getUsername(),
                reservationId,
                request
        ));
    }
}
