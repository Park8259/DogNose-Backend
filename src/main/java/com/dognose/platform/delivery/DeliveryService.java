package com.dognose.platform.delivery;

import com.dognose.platform.reservation.Reservation;
import com.dognose.platform.reservation.ReservationRepository;
import com.dognose.platform.reservation.ReservationResponse;
import com.dognose.platform.reservation.ReservationStatus;
import com.dognose.platform.verification.VerificationRequest;
import com.dognose.platform.verification.VerificationResponse;
import com.dognose.platform.verification.VerificationResult;
import com.dognose.platform.verification.VerificationService;
import com.dognose.platform.verification.VerificationType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliveryService {

    private final ReservationRepository reservationRepository;
    private final VerificationService verificationService;

    public DeliveryService(ReservationRepository reservationRepository, VerificationService verificationService) {
        this.reservationRepository = reservationRepository;
        this.verificationService = verificationService;
    }

    @Transactional
    public DeliveryCompleteResponse completeDelivery(
            String sellerEmail,
            Long reservationId,
            DeliveryCompleteRequest request
    ) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        if (!reservation.getSeller().getEmail().equals(sellerEmail)) {
            throw new IllegalArgumentException("분양자만 인도 완료 처리할 수 있습니다.");
        }

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalArgumentException("확정된 예약만 인도 완료 처리할 수 있습니다.");
        }

        VerificationResponse verification = verificationService.verify(
                sellerEmail,
                reservation.getPost().getDog().getId(),
                new VerificationRequest(
                        request.probeImageUrl(),
                        reservation.getPost().getId(),
                        VerificationType.DELIVERY,
                        request.threshold()
                )
        );

        if (verification.result() != VerificationResult.MATCH) {
            throw new IllegalArgumentException("비문 재검증이 일치하지 않아 인도 완료 처리할 수 없습니다.");
        }

        reservation.complete();
        reservation.getPost().markDelivered();
        reservation.getPost().getDog().markAdopted();

        return new DeliveryCompleteResponse(
                ReservationResponse.from(reservation),
                verification
        );
    }
}
