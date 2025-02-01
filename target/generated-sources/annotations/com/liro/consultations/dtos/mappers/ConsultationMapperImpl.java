package com.liro.consultations.dtos.mappers;

import com.liro.consultations.dtos.ConsultationDTO;
import com.liro.consultations.dtos.RpDTO;
import com.liro.consultations.dtos.responses.ConsultationResponse;
import com.liro.consultations.model.dbentities.Consultation;
import com.liro.consultations.model.dbentities.Rp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-23T20:04:52-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_202 (Oracle Corporation)"
)
@Component
public class ConsultationMapperImpl implements ConsultationMapper {

    @Override
    public ConsultationResponse ConsultationToConsultationResponse(Consultation consultation) {
        if ( consultation == null ) {
            return null;
        }

        ConsultationResponse consultationResponse = new ConsultationResponse();

        consultationResponse.setAnimalId( consultation.getAnimalId() );
        consultationResponse.setTitle( consultation.getTitle() );
        consultationResponse.setAmnsesis( consultation.getAmnsesis() );
        consultationResponse.setClinicalExamination( consultation.getClinicalExamination() );
        consultationResponse.setPresumptiveDiagnosis( consultation.getPresumptiveDiagnosis() );
        consultationResponse.setTreatment( consultation.getTreatment() );
        consultationResponse.setWeight( consultation.getWeight() );
        consultationResponse.setTemperature( consultation.getTemperature() );
        consultationResponse.setRp( rpToRpDTO( consultation.getRp() ) );
        consultationResponse.setId( consultation.getId() );
        consultationResponse.setVetUserId( consultation.getVetUserId() );
        consultationResponse.setLocalDate( consultation.getLocalDate() );

        return consultationResponse;
    }

    @Override
    public Consultation consultationDTOToConsultation(ConsultationDTO consultationDTO) {
        if ( consultationDTO == null ) {
            return null;
        }

        Consultation consultation = new Consultation();

        consultation.setTitle( consultationDTO.getTitle() );
        consultation.setAmnsesis( consultationDTO.getAmnsesis() );
        consultation.setClinicalExamination( consultationDTO.getClinicalExamination() );
        consultation.setPresumptiveDiagnosis( consultationDTO.getPresumptiveDiagnosis() );
        consultation.setTreatment( consultationDTO.getTreatment() );
        consultation.setWeight( consultationDTO.getWeight() );
        consultation.setTemperature( consultationDTO.getTemperature() );
        consultation.setDetails( consultationDTO.getDetails() );
        consultation.setLocalDate( consultationDTO.getLocalDate() );
        consultation.setRp( rpDTOToRp( consultationDTO.getRp() ) );
        consultation.setAnimalId( consultationDTO.getAnimalId() );

        return consultation;
    }

    @Override
    public void updateConsultationFromConsultationDTO(ConsultationDTO consultationDTO, Consultation consultation) {
        if ( consultationDTO == null ) {
            return;
        }

        consultation.setTitle( consultationDTO.getTitle() );
        consultation.setAmnsesis( consultationDTO.getAmnsesis() );
        consultation.setClinicalExamination( consultationDTO.getClinicalExamination() );
        consultation.setPresumptiveDiagnosis( consultationDTO.getPresumptiveDiagnosis() );
        consultation.setTreatment( consultationDTO.getTreatment() );
        consultation.setWeight( consultationDTO.getWeight() );
        consultation.setTemperature( consultationDTO.getTemperature() );
        consultation.setDetails( consultationDTO.getDetails() );
        if ( consultationDTO.getRp() != null ) {
            if ( consultation.getRp() == null ) {
                consultation.setRp( new Rp() );
            }
            rpDTOToRp1( consultationDTO.getRp(), consultation.getRp() );
        }
        else {
            consultation.setRp( null );
        }
    }

    protected RpDTO rpToRpDTO(Rp rp) {
        if ( rp == null ) {
            return null;
        }

        RpDTO rpDTO = new RpDTO();

        List<String> list = rp.getTreatments();
        if ( list != null ) {
            rpDTO.setTreatments( new ArrayList<String>( list ) );
        }
        rpDTO.setNeedToObserve( rp.getNeedToObserve() );
        rpDTO.setControl( rp.getControl() );

        return rpDTO;
    }

    protected Rp rpDTOToRp(RpDTO rpDTO) {
        if ( rpDTO == null ) {
            return null;
        }

        Rp rp = new Rp();

        List<String> list = rpDTO.getTreatments();
        if ( list != null ) {
            rp.setTreatments( new ArrayList<String>( list ) );
        }
        rp.setNeedToObserve( rpDTO.getNeedToObserve() );
        rp.setControl( rpDTO.getControl() );

        return rp;
    }

    protected void rpDTOToRp1(RpDTO rpDTO, Rp mappingTarget) {
        if ( rpDTO == null ) {
            return;
        }

        if ( mappingTarget.getTreatments() != null ) {
            List<String> list = rpDTO.getTreatments();
            if ( list != null ) {
                mappingTarget.getTreatments().clear();
                mappingTarget.getTreatments().addAll( list );
            }
            else {
                mappingTarget.setTreatments( null );
            }
        }
        else {
            List<String> list = rpDTO.getTreatments();
            if ( list != null ) {
                mappingTarget.setTreatments( new ArrayList<String>( list ) );
            }
        }
        mappingTarget.setNeedToObserve( rpDTO.getNeedToObserve() );
        mappingTarget.setControl( rpDTO.getControl() );
    }
}
