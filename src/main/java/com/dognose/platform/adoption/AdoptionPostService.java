package com.dognose.platform.adoption;

import com.dognose.platform.dog.Dog;
import com.dognose.platform.dog.DogRepository;
import com.dognose.platform.user.User;
import com.dognose.platform.user.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdoptionPostService {

    private final AdoptionPostRepository adoptionPostRepository;
    private final DogRepository dogRepository;
    private final UserRepository userRepository;

    public AdoptionPostService(
            AdoptionPostRepository adoptionPostRepository,
            DogRepository dogRepository,
            UserRepository userRepository
    ) {
        this.adoptionPostRepository = adoptionPostRepository;
        this.dogRepository = dogRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AdoptionPostResponse createPost(String sellerEmail, AdoptionPostCreateRequest request) {
        User seller = findUser(sellerEmail);
        Dog dog = findDog(request.dogId());
        validateOwner(sellerEmail, dog);

        if (!dog.isNoseRegistered()) {
            throw new IllegalArgumentException("기준 비문이 등록된 반려견만 분양글을 등록할 수 있습니다.");
        }

        AdoptionPost post = new AdoptionPost(
                dog,
                seller,
                request.title(),
                request.content(),
                request.price(),
                request.region(),
                request.adoptionReason(),
                request.contractTerms()
        );

        return AdoptionPostResponse.from(adoptionPostRepository.save(post));
    }

    @Transactional(readOnly = true)
    public List<AdoptionPostResponse> getOpenPosts(String region) {
        List<AdoptionPost> posts = region == null || region.isBlank()
                ? adoptionPostRepository.findAllByStatusOrderByCreatedAtDesc(AdoptionPostStatus.OPEN)
                : adoptionPostRepository.findAllByRegionContainingAndStatusOrderByCreatedAtDesc(
                        region,
                        AdoptionPostStatus.OPEN
                );

        return posts.stream()
                .map(AdoptionPostResponse::from)
                .toList();
    }

    @Transactional
    public AdoptionPostResponse getPost(Long postId) {
        AdoptionPost post = adoptionPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("분양글을 찾을 수 없습니다."));
        post.increaseViewCount();
        return AdoptionPostResponse.from(post);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private Dog findDog(Long dogId) {
        return dogRepository.findById(dogId)
                .orElseThrow(() -> new IllegalArgumentException("반려견을 찾을 수 없습니다."));
    }

    private void validateOwner(String ownerEmail, Dog dog) {
        if (!dog.getOwner().getEmail().equals(ownerEmail)) {
            throw new IllegalArgumentException("본인이 등록한 반려견만 분양글을 등록할 수 있습니다.");
        }
    }
}
