package com.liro.consultations.dtos.mappers;
import com.liro.consultations.dtos.ConsultationDTO;
import com.liro.consultations.dtos.responses.ConsultationResponse;
import com.liro.consultations.dtos.responses.RpResponse;
import com.liro.consultations.model.dbentities.Consultation;
import com.liro.consultations.model.dbentities.Rp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import javax.persistence.Column;
import java.time.LocalDate;


@Mapper(componentModel = "spring")
public interface ConsultationMapper {

    @Mapping(target = "details", ignore = true)
    ConsultationResponse ConsultationToConsultationResponse(Consultation consultation);


    Consultation consultationDTOToConsultation(ConsultationDTO consultationDTO);

    @Mapping(target = "vetUserId", ignore = true)
    @Mapping(target = "vetClinicId", ignore = true)
    @Mapping(target = "localDate", ignore = true)
    @Mapping(target = "animalId", ignore = true)
    void updateConsultationFromConsultationDTO(ConsultationDTO consultationDTO, @MappingTarget Consultation consultation);


    @Mapping(target = "animalId", source = "consultation.animalId")
    @Mapping(target = "clinicId", source = "consultation.vetClinicId")
    RpResponse rpToRpResponse(Rp Rp);

}

