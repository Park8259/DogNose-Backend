package com.dognose.platform.dog;

import com.dognose.platform.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {

    List<Dog> findAllByOwnerOrderByCreatedAtDesc(User owner);
}
