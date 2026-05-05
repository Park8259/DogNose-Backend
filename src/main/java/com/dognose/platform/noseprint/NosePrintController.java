package com.dognose.platform.noseprint;

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
@RequestMapping("/api/dogs/{dogId}/nose-prints")
public class NosePrintController {

    private final NosePrintService nosePrintService;

    public NosePrintController(NosePrintService nosePrintService) {
        this.nosePrintService = nosePrintService;
    }

    @PostMapping
    public ApiResponse<NosePrintResponse> createNosePrint(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long dogId,
            @Valid @RequestBody NosePrintCreateRequest request
    ) {
        return ApiResponse.ok(nosePrintService.createNosePrint(userDetails.getUsername(), dogId, request));
    }

    @GetMapping
    public ApiResponse<List<NosePrintResponse>> getDogNosePrints(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long dogId
    ) {
        return ApiResponse.ok(nosePrintService.getDogNosePrints(userDetails.getUsername(), dogId));
    }
}
