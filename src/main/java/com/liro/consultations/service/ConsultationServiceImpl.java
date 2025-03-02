package com.liro.consultations.service;

import com.liro.consultations.dtos.RecordDTO;
import com.liro.consultations.dtos.UserDTO;
import com.liro.consultations.dtos.responses.LastConsultationResponse;
import com.liro.consultations.dtos.responses.RpResponse;
import com.liro.consultations.exceptions.ResourceNotFoundException;
import com.liro.consultations.model.dbentities.Rp;
import com.liro.consultations.repositories.ConsultationRepository;
import com.liro.consultations.config.FeignAnimalClient;
import com.liro.consultations.dtos.ConsultationDTO;
import com.liro.consultations.dtos.mappers.ConsultationMapper;
import com.liro.consultations.dtos.responses.ConsultationResponse;
import com.liro.consultations.exceptions.BadRequestException;
import com.liro.consultations.exceptions.UnauthorizedException;
import com.liro.consultations.model.dbentities.Consultation;
import com.liro.consultations.repositories.RpRepository;
import com.liro.consultations.util.Util;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.liro.consultations.util.Util.getUser;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    @Autowired
    FeignAnimalClient feignAnimalClient;

    @Autowired
    ConsultationMapper consultationMapper;

    @Autowired
    ConsultationRepository consultationRepository;

    @Autowired
    RpRepository rpRepository;

    @Override
    public Page<ConsultationResponse> findAllByAnimalId(Long animalId, Pageable pageable, String token, Long clinicId) {

        return consultationRepository.findAllByAnimalId(animalId, pageable)
                .map(consultation -> {
                    ConsultationResponse consultationResponse = consultationMapper.ConsultationToConsultationResponse(consultation);


                    System.out.println("paso");
                    if (clinicId != null && clinicId.equals(consultation.getVetClinicId())) {
                        System.out.println("entro");
                        consultationResponse.setDetails(consultation.getDetails());
                    }
                    return consultationResponse;
                });
    }

    @Override
    public ConsultationResponse getConsultationResponse(Long consultationId, String token, Long clinidId) {

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new BadRequestException("Consultation not found"));

        ConsultationResponse consultationResponse = consultationMapper.ConsultationToConsultationResponse(consultation);

        if (clinidId.equals(consultation.getVetClinicId())) {
            consultationResponse.setDetails(consultation.getDetails());
        }
        return consultationResponse;
    }

    @Override
    public List<RpResponse> getNextControls(List<Long> animalIds) {
        LocalDateTime nextDay = LocalDate.now().plusDays(1).atStartOfDay();
        return rpRepository.findRpsWithControlNextDay(nextDay, animalIds)
                .stream().map(rp -> consultationMapper.rpToRpResponse(rp)).collect(Collectors.toList());
    }

    @Override
    public LastConsultationResponse getLastConsultationResponse(Long animalId, String token, Long clinidId) {

        UserDTO userDTO = getUser(token);
        LastConsultationResponse lastConsultationResponse = new LastConsultationResponse();

        Consultation lastFound = consultationRepository.findTopByAnimalIdAndVetClinicIdOrderByLocalDateDesc(animalId, clinidId).orElseThrow(
                () -> new ResourceNotFoundException("No queries have been found for the animal " +
                        animalId + ", with the veterinarian" + userDTO.getId()));

        lastConsultationResponse.setTotalConsultations(consultationRepository.countByAnimalIdAndVetClinicId(animalId, clinidId));
        lastConsultationResponse.setTitle(lastFound.getTitle());
        lastConsultationResponse.setLastConsultationInDays(ChronoUnit.DAYS.between(lastFound.getLocalDate(), LocalDate.now()));
        return lastConsultationResponse;
    }

    @Transactional
    @Override
    public ConsultationResponse createConsultation(ConsultationDTO consultationDTO, String token, Long clinicId) {

        feignAnimalClient.hasPermissions(consultationDTO.getAnimalId(), false,
                false, true, clinicId, token);

        UserDTO userDTO = getUser(token);

        if (!userDTO.getRoles().contains("ROLE_VET")) {
            throw new UnauthorizedException("Consultation must be created by a vet");
        }

        Consultation consultation = consultationMapper.consultationDTOToConsultation(consultationDTO);
        consultation.setVetUserId(userDTO.getId());
        consultation.setLocalDate(LocalDate.now());
        consultation.setVetClinicId(clinicId);


        if (consultationDTO.getWeight() != null) {
            RecordDTO recordDTO = RecordDTO.builder()
                    .date(LocalDateTime.now())
                    .dataString(String.valueOf(consultationDTO.getWeight()))
                    .recordTypeId(3L)
                    .details(null)
                    .animalId(consultationDTO.getAnimalId())
                    .build();

            feignAnimalClient.createRecord(recordDTO, clinicId, token);
        }

        if (consultationDTO.getRp() != null) {
            Rp rp = Rp.builder()
                    .treatments(consultationDTO.getRp().getTreatments())
                    .needToObserve(consultationDTO.getRp().getNeedToObserve())
                    .control(consultationDTO.getRp().getControl())
                    .consultation(consultation) // Link RP to Consultation
                    .build();

            consultation.setRp(rp);
        }

        Consultation savedConsultation = consultationRepository.save(consultation);

        // Return response
        return consultationMapper.ConsultationToConsultationResponse(savedConsultation);
    }


    @Override
    public Void migrateConsultations(List<ConsultationDTO> consultationDTOs, Long vetClinicId, Long vetUserId) {

        List<RecordDTO> recordDTOs = new ArrayList<>();
        consultationDTOs.stream().parallel().forEach(consultationDTO -> {

            try {
                Consultation consultation = consultationMapper.consultationDTOToConsultation(consultationDTO);
                consultation.setVetClinicId(vetClinicId);
                consultation.setVetUserId(vetUserId);

                consultation.setLocalDate(consultationDTO.getLocalDate());

                if (consultationDTO.getLocalDate() != null && consultationDTO.getWeight() != null) {
                    RecordDTO recordDTO = RecordDTO.builder()
                            .date(consultationDTO.getLocalDate().atStartOfDay())
                            .dataString(String.valueOf(consultationDTO.getWeight()))
                            .recordTypeId(25L)
                            .details(null)
                            .animalId(consultationDTO.getAnimalId())
                            .build();
                    recordDTOs.add(recordDTO);
                }

                consultationRepository.save(consultation);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });

        feignAnimalClient.migrateRecords(recordDTOs, vetUserId);

        return null;
    }


    //TODO
    @Override
    public void updateConsultation(ConsultationDTO consultationDto, Long consultationId, String token, Long clinicId) {

        feignAnimalClient.hasPermissions(consultationDto.getAnimalId(), false,
                false, true, clinicId, token);


        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new BadRequestException("Consultation not found"));

        if (clinicId.equals(consultation.getVetClinicId())) {
            consultationMapper.updateConsultationFromConsultationDTO(consultationDto, consultation);
        } else throw new BadRequestException("You do not have permission to modify the consultation!!");

        consultationRepository.save(consultation);
    }
}
