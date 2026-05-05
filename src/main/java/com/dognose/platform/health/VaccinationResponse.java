package com.dognose.platform.health;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record VaccinationResponse(
        Long id,
        Long dogId,
        String vaccineName,
        LocalDate vaccinatedAt,
        LocalDate nextDueAt,
        String hospitalName,
        String attachmentUrl,
        LocalDateTime createdAt
) {
    public static VaccinationResponse from(Vaccination vaccination) {
        return new VaccinationResponse(
                vaccination.getId(),
                vaccination.getDog().getId(),
                vaccination.getVaccineName(),
                vaccination.getVaccinatedAt(),
                vaccination.getNextDueAt(),
                vaccination.getHospitalName(),
                vaccination.getAttachmentUrl(),
                vaccination.getCreatedAt()
        );
    }
}
