package com.dognose.platform.adoption;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionPostRepository extends JpaRepository<AdoptionPost, Long> {

    List<AdoptionPost> findAllByOrderByCreatedAtDesc();

    List<AdoptionPost> findAllByStatusOrderByCreatedAtDesc(AdoptionPostStatus status);

    List<AdoptionPost> findAllByRegionContainingAndStatusOrderByCreatedAtDesc(String region, AdoptionPostStatus status);
}
