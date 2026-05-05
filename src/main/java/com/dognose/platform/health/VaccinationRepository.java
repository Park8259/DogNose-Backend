package com.dognose.platform.health;

import com.dognose.platform.dog.Dog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {

    List<Vaccination> findAllByDogOrderByVaccinatedAtDescCreatedAtDesc(Dog dog);
}
