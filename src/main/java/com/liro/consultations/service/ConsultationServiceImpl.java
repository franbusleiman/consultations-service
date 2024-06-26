package com.liro.consultations.service;

import com.liro.consultations.dtos.RecordDTO;
import com.liro.consultations.repositories.ConsultationRepository;
import com.liro.consultations.config.FeignAnimalClient;
import com.liro.consultations.dtos.ConsultationDTO;
import com.liro.consultations.dtos.mappers.ConsultationMapper;
import com.liro.consultations.dtos.responses.ConsultationResponse;
import com.liro.consultations.exceptions.BadRequestException;
import com.liro.consultations.model.dbentities.Consultation;
import org.bouncycastle.asn1.cms.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.liro.consultations.util.Util.updateIfNotNull;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    @Autowired
    FeignAnimalClient feignAnimalClient;

    @Autowired
    ConsultationMapper consultationMapper;

    @Autowired
    ConsultationRepository consultationRepository;

    @Override
    public Page<ConsultationResponse> findAllByAnimalId(Long animalId, Pageable pageable, String token) {

        ResponseEntity<Void> response = feignAnimalClient.hasPermissions(
                animalId, false, false, true, false, token);

        if (response.getStatusCode().is2xxSuccessful()) {
        return consultationRepository.findAllByAnimalId(animalId, pageable)
                .map(consultation -> consultationMapper.ConsultationToConsultationResponse(consultation));

        } else throw new BadRequestException("User has no permissions on animal");
    }

    @Override
    public ConsultationResponse getConsultationResponse(Long consultationId, String token) {

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(()-> new BadRequestException("Consultation not found"));

        ResponseEntity<Void> response = feignAnimalClient.hasPermissions(
                consultation.getAnimalId(), false, false, true, false, token);

        if (response.getStatusCode().is2xxSuccessful()) {

            return consultationMapper.ConsultationToConsultationResponse(consultation);
        } else throw new BadRequestException("User has no permissions on animal");
    }

    @Override
    public ConsultationResponse createConsultation(ConsultationDTO consultationDTO, String token) {
        ResponseEntity<Void> response = feignAnimalClient.hasPermissions(
                consultationDTO.getAnimalId(), true, false, true, false, token);

        if (response.getStatusCode().is2xxSuccessful()) {
            if (consultationDTO.getDetails() != null) {
                consultationDTO.setDetails(consultationDTO.getDetails().toLowerCase());
            }

            Consultation consultation = consultationMapper.consultationDTOToConsultation(consultationDTO);

            consultation.setLocalDate(LocalDate.now());

            RecordDTO recordDTO = RecordDTO.builder()
                    .date(LocalDateTime.now())
                    .dataString("Weight: " + consultationDTO.getWeight())
                    .recordTypeId(3L)
                    .details(consultationDTO.getDetails())
                    .animalId(consultationDTO.getAnimalId())
                    .build();

            feignAnimalClient.createRecord(recordDTO, token);

            return consultationMapper.ConsultationToConsultationResponse(consultationRepository.save(consultation));
        } else throw new BadRequestException("User has no permissions on animal");
    }

    @Override
    public void updateConsultation(ConsultationDTO consultationDto, Long consultationId, String token) {
        ResponseEntity<Void> response = feignAnimalClient.hasPermissions(
                consultationDto.getAnimalId(), true, false, true, false, token);

        if (response.getStatusCode().is2xxSuccessful()) {
            if (consultationDto.getDetails() != null) {
                consultationDto.setDetails(consultationDto.getDetails().toLowerCase());
            }

            Consultation consultation = consultationRepository.findById(consultationId)
                    .orElseThrow(()-> new BadRequestException("Consultation not found"));

            // updateIfNotNull(consultation::setConsultationType, consultationDto.getConsultationType());
            // updateIfNotNull(consultation::setDetails, consultationDto.getDetails());

            consultationRepository.save(consultation);
        } else throw new BadRequestException("User has no permissions on animal");
    }
}
