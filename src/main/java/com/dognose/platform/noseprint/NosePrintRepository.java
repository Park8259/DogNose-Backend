package com.dognose.platform.noseprint;

import com.dognose.platform.dog.Dog;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NosePrintRepository extends JpaRepository<NosePrint, Long> {

    List<NosePrint> findAllByDogOrderByCreatedAtDesc(Dog dog);

    Optional<NosePrint> findFirstByDogAndReferenceTrueOrderByCreatedAtDesc(Dog dog);
}
