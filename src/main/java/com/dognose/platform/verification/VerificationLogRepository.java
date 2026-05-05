package com.dognose.platform.verification;

import com.dognose.platform.dog.Dog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationLogRepository extends JpaRepository<VerificationLog, Long> {

    List<VerificationLog> findAllByDogOrderByCreatedAtDesc(Dog dog);

    List<VerificationLog> findAllByOrderByCreatedAtDesc();

    List<VerificationLog> findAllByResultOrderByCreatedAtDesc(VerificationResult result);
}
