package com.dognose.platform.adoption;

import com.dognose.platform.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionApplicationRepository extends JpaRepository<AdoptionApplication, Long> {

    boolean existsByPostAndApplicant(AdoptionPost post, User applicant);

    List<AdoptionApplication> findAllByPostOrderByCreatedAtDesc(AdoptionPost post);

    List<AdoptionApplication> findAllByApplicantOrderByCreatedAtDesc(User applicant);
}
