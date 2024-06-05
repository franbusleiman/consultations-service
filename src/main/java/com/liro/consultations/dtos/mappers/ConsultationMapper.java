package main.java.com.liro.consultations.dtos.mappers;
import main.java.com.liro.consultations.dtos.ConsultationDTO;
import main.java.com.liro.consultations.dtos.responses.ConsultationResponse;
import main.java.com.liro.consultations.model.dbentities.Consultation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ConsultationMapper {

    ConsultationResponse ConsultationToConsultationResponse(Consultation consultation);


    Consultation consultationDTOToConsultation(ConsultationDTO consultationDTO);
}
