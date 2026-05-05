package com.dognose.platform.reservation;

import com.dognose.platform.adoption.AdoptionApplication;
import com.dognose.platform.adoption.AdoptionApplicationRepository;
import com.dognose.platform.adoption.ApplicationStatus;
import com.dognose.platform.user.User;
import com.dognose.platform.user.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AdoptionApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            AdoptionApplicationRepository applicationRepository,
            UserRepository userRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ReservationResponse createReservation(String requesterEmail, ReservationCreateRequest request) {
        AdoptionApplication application = findApplication(request.applicationId());

        if (application.getStatus() != ApplicationStatus.ACCEPTED) {
            throw new IllegalArgumentException("승인된 입양 신청만 예약을 만들 수 있습니다.");
        }

        validateParticipant(requesterEmail, application);

        Reservation reservation = new Reservation(application, request.reservedAt(), request.place());
        return ReservationResponse.from(reservationRepository.save(reservation));
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getMyReservations(String userEmail) {
        User user = findUser(userEmail);
        return reservationRepository.findAllBySellerOrApplicantOrderByReservedAtDesc(user, user)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse confirm(String sellerEmail, Long reservationId) {
        Reservation reservation = findReservation(reservationId);
        validateSeller(sellerEmail, reservation);
        reservation.confirm();
        return ReservationResponse.from(reservation);
    }

    @Transactional
    public ReservationResponse cancel(String requesterEmail, Long reservationId) {
        Reservation reservation = findReservation(reservationId);
        validateParticipant(requesterEmail, reservation.getApplication());
        reservation.cancel();
        return ReservationResponse.from(reservation);
    }

    @Transactional
    public ReservationResponse complete(String sellerEmail, Long reservationId) {
        Reservation reservation = findReservation(reservationId);
        validateSeller(sellerEmail, reservation);
        reservation.complete();
        return ReservationResponse.from(reservation);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private AdoptionApplication findApplication(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("입양 신청을 찾을 수 없습니다."));
    }

    private Reservation findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
    }

    private void validateParticipant(String requesterEmail, AdoptionApplication application) {
        boolean isSeller = application.getPost().getSeller().getEmail().equals(requesterEmail);
        boolean isApplicant = application.getApplicant().getEmail().equals(requesterEmail);
        if (!isSeller && !isApplicant) {
            throw new IllegalArgumentException("예약 참여자만 처리할 수 있습니다.");
        }
    }

    private void validateSeller(String sellerEmail, Reservation reservation) {
        if (!reservation.getSeller().getEmail().equals(sellerEmail)) {
            throw new IllegalArgumentException("분양자만 처리할 수 있습니다.");
        }
    }
}
