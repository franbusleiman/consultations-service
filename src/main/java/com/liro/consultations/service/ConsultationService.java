package com.liro.consultations.service;

import com.liro.consultations.dtos.ConsultationDTO;
import com.liro.consultations.dtos.UserDTO;
import com.liro.consultations.dtos.responses.ConsultationResponse;
import com.liro.consultations.dtos.responses.LastConsultationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConsultationService {

    Page<ConsultationResponse> findAllByAnimalId(Long animalId, Pageable pageable, String token);

    ConsultationResponse getConsultationResponse(Long consultationId, String token);
    LastConsultationResponse getLastConsultationResponse(Long animalId, String token);

    ConsultationResponse createConsultation(ConsultationDTO consultationDTO, String token);

    Void migrateConsultations(List<ConsultationDTO> consultationDTOs, Long vetUserId);


    void updateConsultation(ConsultationDTO consultationDto, Long consultationId, String token);
}
