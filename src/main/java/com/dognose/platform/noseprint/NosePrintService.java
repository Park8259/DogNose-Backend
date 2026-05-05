package com.dognose.platform.noseprint;

import com.dognose.platform.dog.Dog;
import com.dognose.platform.dog.DogRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NosePrintService {

    private static final String DEFAULT_EMBEDDING_MODEL = "mock-s101_224";

    private final NosePrintRepository nosePrintRepository;
    private final DogRepository dogRepository;

    public NosePrintService(NosePrintRepository nosePrintRepository, DogRepository dogRepository) {
        this.nosePrintRepository = nosePrintRepository;
        this.dogRepository = dogRepository;
    }

    @Transactional
    public NosePrintResponse createNosePrint(String ownerEmail, Long dogId, NosePrintCreateRequest request) {
        Dog dog = findDog(dogId);
        validateOwner(ownerEmail, dog);

        boolean reference = request.reference() == null || request.reference();
        String embeddingModel = request.embeddingModel() == null || request.embeddingModel().isBlank()
                ? DEFAULT_EMBEDDING_MODEL
                : request.embeddingModel();

        NosePrint nosePrint = new NosePrint(
                dog,
                request.imageUrl(),
                request.vectorPointId(),
                request.qualityScore(),
                embeddingModel,
                reference,
                NosePrintStatus.VALID
        );

        if (reference) {
            dog.markNoseRegistered();
        }

        return NosePrintResponse.from(nosePrintRepository.save(nosePrint));
    }

    @Transactional(readOnly = true)
    public List<NosePrintResponse> getDogNosePrints(String ownerEmail, Long dogId) {
        Dog dog = findDog(dogId);
        validateOwner(ownerEmail, dog);

        return nosePrintRepository.findAllByDogOrderByCreatedAtDesc(dog)
                .stream()
                .map(NosePrintResponse::from)
                .toList();
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
