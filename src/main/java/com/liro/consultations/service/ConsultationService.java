package main.java.com.liro.consultations.service;

import main.java.com.liro.consultations.dtos.ConsultationDTO;
import main.java.com.liro.consultations.dtos.UserDTO;
import main.java.com.liro.consultations.dtos.responses.ConsultationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConsultationService {

    Page<ConsultationResponse> findAllByAnimalId(Long animalId, Pageable pageable, String token);

    ConsultationResponse getConsultationResponse(Long consultationId, String token);

    ConsultationResponse createConsultation(ConsultationDTO consultationDTO, String token);

    void updateConsultation(ConsultationDTO consultationDto, Long consultationId, String token);
}
