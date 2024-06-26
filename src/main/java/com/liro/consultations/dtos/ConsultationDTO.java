package com.liro.consultations.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import com.liro.consultations.model.enums.ConsultationType;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ConsultationDTO {

    private String details;
    private Long animalId;
    private String title;
    private String amnsesis;
    private String clinicalExamination;
    private String presumptiveDiagnosis;
    private String treatment;
    private Double weight;
    private Double temperature;
}
