package com.dognose.platform.dog;

import com.dognose.platform.user.User;
import com.dognose.platform.user.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DogService {

    private final DogRepository dogRepository;
    private final UserRepository userRepository;

    public DogService(DogRepository dogRepository, UserRepository userRepository) {
        this.dogRepository = dogRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public DogResponse createDog(String ownerEmail, DogCreateRequest request) {
        User owner = findUserByEmail(ownerEmail);

        Dog dog = new Dog(
                owner,
                request.name(),
                request.breed(),
                request.gender(),
                request.birthDate(),
                request.description(),
                request.profileImageUrl()
        );

        return DogResponse.from(dogRepository.save(dog));
    }

    @Transactional(readOnly = true)
    public List<DogResponse> getMyDogs(String ownerEmail) {
        User owner = findUserByEmail(ownerEmail);
        return dogRepository.findAllByOwnerOrderByCreatedAtDesc(owner)
                .stream()
                .map(DogResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DogResponse getDog(Long dogId) {
        Dog dog = dogRepository.findById(dogId)
                .orElseThrow(() -> new IllegalArgumentException("반려견을 찾을 수 없습니다."));
        return DogResponse.from(dog);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}
