package com.dognose.platform.health;

import com.dognose.platform.dog.Dog;
import com.dognose.platform.dog.DogRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HealthService {

    private final DogRepository dogRepository;
    private final HealthRecordRepository healthRecordRepository;
    private final VaccinationRepository vaccinationRepository;

    public HealthService(
            DogRepository dogRepository,
            HealthRecordRepository healthRecordRepository,
            VaccinationRepository vaccinationRepository
    ) {
        this.dogRepository = dogRepository;
        this.healthRecordRepository = healthRecordRepository;
        this.vaccinationRepository = vaccinationRepository;
    }

    @Transactional
    public HealthRecordResponse createHealthRecord(
            String ownerEmail,
            Long dogId,
            HealthRecordCreateRequest request
    ) {
        Dog dog = findDog(dogId);
        validateOwner(ownerEmail, dog);

        HealthRecord record = new HealthRecord(
                dog,
                request.recordType(),
                request.title(),
                request.description(),
                request.recordDate(),
                request.attachmentUrl()
        );

        return HealthRecordResponse.from(healthRecordRepository.save(record));
    }

    @Transactional(readOnly = true)
    public List<HealthRecordResponse> getHealthRecords(Long dogId) {
        Dog dog = findDog(dogId);
        return healthRecordRepository.findAllByDogOrderByRecordDateDescCreatedAtDesc(dog)
                .stream()
                .map(HealthRecordResponse::from)
                .toList();
    }

    @Transactional
    public VaccinationResponse createVaccination(
            String ownerEmail,
            Long dogId,
            VaccinationCreateRequest request
    ) {
        Dog dog = findDog(dogId);
        validateOwner(ownerEmail, dog);

        Vaccination vaccination = new Vaccination(
                dog,
                request.vaccineName(),
                request.vaccinatedAt(),
                request.nextDueAt(),
                request.hospitalName(),
                request.attachmentUrl()
        );

        return VaccinationResponse.from(vaccinationRepository.save(vaccination));
    }

    @Transactional(readOnly = true)
    public List<VaccinationResponse> getVaccinations(Long dogId) {
        Dog dog = findDog(dogId);
        return vaccinationRepository.findAllByDogOrderByVaccinatedAtDescCreatedAtDesc(dog)
                .stream()
                .map(VaccinationResponse::from)
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
