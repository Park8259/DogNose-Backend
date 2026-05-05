package com.dognose.platform.reservation;

import com.dognose.platform.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllBySellerOrApplicantOrderByReservedAtDesc(User seller, User applicant);
}
