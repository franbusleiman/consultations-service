package com.liro.consultations.dtos.mappers;
import com.liro.consultations.dtos.ConsultationDTO;
import com.liro.consultations.dtos.responses.ConsultationResponse;
import com.liro.consultations.model.dbentities.Consultation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring")
public interface ConsultationMapper {

    @Mapping(target = "description", ignore = true)
    ConsultationResponse ConsultationToConsultationResponse(Consultation consultation);


    Consultation consultationDTOToConsultation(ConsultationDTO consultationDTO);
}

