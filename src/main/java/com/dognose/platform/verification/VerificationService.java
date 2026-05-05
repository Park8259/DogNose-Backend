package com.dognose.platform.verification;

import com.dognose.platform.dog.Dog;
import com.dognose.platform.dog.DogRepository;
import com.dognose.platform.noseprint.NosePrint;
import com.dognose.platform.noseprint.NosePrintRepository;
import com.dognose.platform.user.User;
import com.dognose.platform.user.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VerificationService {

    private static final BigDecimal DEFAULT_THRESHOLD = new BigDecimal("0.750000");

    private final VerificationLogRepository verificationLogRepository;
    private final DogRepository dogRepository;
    private final UserRepository userRepository;
    private final NosePrintRepository nosePrintRepository;
    private final NosePrintMatcher nosePrintMatcher;

    public VerificationService(
            VerificationLogRepository verificationLogRepository,
            DogRepository dogRepository,
            UserRepository userRepository,
            NosePrintRepository nosePrintRepository,
            NosePrintMatcher nosePrintMatcher
    ) {
        this.verificationLogRepository = verificationLogRepository;
        this.dogRepository = dogRepository;
        this.userRepository = userRepository;
        this.nosePrintRepository = nosePrintRepository;
        this.nosePrintMatcher = nosePrintMatcher;
    }

    @Transactional
    public VerificationResponse verify(String requesterEmail, Long dogId, VerificationRequest request) {
        User requester = findUser(requesterEmail);
        Dog dog = findDog(dogId);
        validateOwner(requesterEmail, dog);

        NosePrint referenceNosePrint = nosePrintRepository.findFirstByDogAndReferenceTrueOrderByCreatedAtDesc(dog)
                .orElseThrow(() -> new IllegalArgumentException("기준 비문이 등록되지 않았습니다."));

        BigDecimal threshold = request.threshold() == null ? DEFAULT_THRESHOLD : request.threshold();
        NosePrintMatchResult matchResult = nosePrintMatcher.verify(
                referenceNosePrint.getImageUrl(),
                request.probeImageUrl(),
                threshold
        );

        VerificationLog log = new VerificationLog(
                dog,
                request.postId(),
                requester,
                referenceNosePrint,
                request.probeImageUrl(),
                request.verificationType(),
                matchResult.cosineSimilarity(),
                matchResult.euclideanDistance(),
                matchResult.threshold(),
                matchResult.result(),
                matchResult.modelName()
        );

        return VerificationResponse.from(verificationLogRepository.save(log));
    }

    @Transactional(readOnly = true)
    public List<VerificationResponse> getDogVerificationLogs(String requesterEmail, Long dogId) {
        Dog dog = findDog(dogId);
        validateOwner(requesterEmail, dog);

        return verificationLogRepository.findAllByDogOrderByCreatedAtDesc(dog)
                .stream()
                .map(VerificationResponse::from)
                .toList();
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
            throw new IllegalArgumentException("본인이 등록한 반려견만 처리할 수 있습니다.");
        }
    }
}
