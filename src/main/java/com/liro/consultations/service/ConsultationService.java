package com.liro.consultations.service;

import com.liro.consultations.dtos.ConsultationDTO;
import com.liro.consultations.dtos.UserDTO;
import com.liro.consultations.dtos.responses.ConsultationResponse;
import com.liro.consultations.dtos.responses.LastConsultationResponse;
import com.liro.consultations.dtos.responses.RpResponse;
import com.liro.consultations.model.dbentities.Rp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConsultationService {

    Page<ConsultationResponse> findAllByAnimalId(Long animalId, Pageable pageable, String token, Long clinidId);

    ConsultationResponse getConsultationResponse(Long consultationId, String token, Long clinidId);

    List<RpResponse> getNextControls(List<Long> animalIds);

    LastConsultationResponse getLastConsultationResponse(Long animalId, String token, Long clinidId);

    ConsultationResponse createConsultation(ConsultationDTO consultationDTO, String token, Long clinicId);

    Void migrateConsultations(List<ConsultationDTO> consultationDTOs, Long vetClinicId, Long vetUserId);

    void updateConsultation(ConsultationDTO consultationDto, Long consultationId, String token, Long clinidId);

    void deleteConsultation(Long consultationId, Long clinicId, String token);

    void deleteAllConsultationsByAnimalId(Long animalId, Long clinicId);

}
