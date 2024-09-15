package com.liro.consultations.service;

import com.liro.consultations.dtos.RecordDTO;
import com.liro.consultations.dtos.UserDTO;
import com.liro.consultations.dtos.responses.LastConsultationResponse;
import com.liro.consultations.exceptions.ResourceNotFoundException;
import com.liro.consultations.repositories.ConsultationRepository;
import com.liro.consultations.config.FeignAnimalClient;
import com.liro.consultations.dtos.ConsultationDTO;
import com.liro.consultations.dtos.mappers.ConsultationMapper;
import com.liro.consultations.dtos.responses.ConsultationResponse;
import com.liro.consultations.exceptions.BadRequestException;
import com.liro.consultations.exceptions.UnauthorizedException;
import com.liro.consultations.model.dbentities.Consultation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.liro.consultations.util.Util.getUser;

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
        UserDTO userDTO = getUser(token);

        return consultationRepository.findAllByAnimalId(animalId, pageable)
                .map(consultation -> {
                    ConsultationResponse consultationResponse = consultationMapper.ConsultationToConsultationResponse(consultation);
                    if (userDTO.getId().equals(consultation.getVetUserId())) {
                        consultationResponse.setDetails(consultation.getDetails());
                    }
                    return consultationResponse;
                });
    }

    @Override
    public ConsultationResponse getConsultationResponse(Long consultationId, String token) {
        UserDTO userDTO = getUser(token);

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new BadRequestException("Consultation not found"));

        ConsultationResponse consultationResponse = consultationMapper.ConsultationToConsultationResponse(consultation);

        if (userDTO.getId().equals(consultation.getVetUserId())) {
            consultationResponse.setDetails(consultation.getDetails());
        }
        return consultationResponse;
    }

    @Override
    public LastConsultationResponse getLastConsultationResponse(Long animalId, String token) {
        ResponseEntity<Void> response = feignAnimalClient.hasPermissions(animalId, false, false, true, false, token);

        UserDTO userDTO = getUser(token);
        LastConsultationResponse lastConsultationResponse = new LastConsultationResponse();

        if (response.getStatusCode().is2xxSuccessful()) {
            Consultation lastFound = consultationRepository.findTopByAnimalIdAndVetUserIdOrderByLocalDateDesc(animalId, userDTO.getId()).orElseThrow(
                    () -> new ResourceNotFoundException("No queries have been found for the animal " +
                            animalId + ", with the veterinarian" + userDTO.getId()));

            lastConsultationResponse.setTotalConsultations(consultationRepository.countByAnimalIdAndVetUserId(animalId, userDTO.getId()));
            lastConsultationResponse.setTitle(lastFound.getTitle());
            lastConsultationResponse.setLastConsultationInDays(ChronoUnit.DAYS.between(lastFound.getLocalDate(), LocalDate.now()));
            return lastConsultationResponse;
        } else {
            throw new BadRequestException("User has no permissions on animal");
        }
    }

    @Override
    public ConsultationResponse createConsultation(ConsultationDTO consultationDTO, String token) {
        UserDTO userDTO = getUser(token);

        if (!userDTO.getRoles().contains("ROLE_VET")) {
            throw new UnauthorizedException("Consultation must be created by a vet");
        }

        Consultation consultation = consultationMapper.consultationDTOToConsultation(consultationDTO);
        consultation.setVetUserId(userDTO.getId());
        consultation.setLocalDate(LocalDate.now());

        RecordDTO recordDTO = RecordDTO.builder()
                .date(LocalDateTime.now())
                .dataString(String.valueOf(consultationDTO.getWeight()))
                .recordTypeId(3L)
                .details(null)
                .animalId(consultationDTO.getAnimalId())
                .build();

        feignAnimalClient.createRecord(recordDTO, token);

        return consultationMapper.ConsultationToConsultationResponse(consultationRepository.save(consultation));
    }

    @Override
    public Void migrateConsultations(List<ConsultationDTO> consultationDTOs, Long vetUserId) {

        List<RecordDTO> recordDTOs = new ArrayList<>();
        consultationDTOs.forEach(consultationDTO -> {

            Consultation consultation = consultationMapper.consultationDTOToConsultation(consultationDTO);
            consultation.setVetUserId(vetUserId);

            consultation.setLocalDate(LocalDate.now());

            RecordDTO recordDTO = RecordDTO.builder()
                    .date(consultationDTO.getLocalDate().atStartOfDay())
                    .dataString(String.valueOf(consultationDTO.getWeight()))
                    .recordTypeId(3L)
                    .details(null)
                    .animalId(consultationDTO.getAnimalId())
                    .build();


            consultationRepository.save(consultation);

            recordDTOs.add(recordDTO);
        });

        feignAnimalClient.migrateRecords(recordDTOs, vetUserId);

        return null;
    }

    @Override
    public void updateConsultation(ConsultationDTO consultationDto, Long consultationId, String token) {
        UserDTO userDTO = getUser(token);

        if (!userDTO.getRoles().contains("ROLE_VET")) {
            throw new UnauthorizedException("Consultation must be created by a vet");
        }

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new BadRequestException("Consultation not found"));

        // updateIfNotNull(consultation::setConsultationType, consultationDto.getConsultationType());
        // updateIfNotNull(consultation::setDetails, consultationDto.getDetails());

        consultationRepository.save(consultation);
    }
}
