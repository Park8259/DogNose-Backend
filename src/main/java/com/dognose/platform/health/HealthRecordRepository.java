package com.dognose.platform.health;

import com.dognose.platform.dog.Dog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {

    List<HealthRecord> findAllByDogOrderByRecordDateDescCreatedAtDesc(Dog dog);
}
