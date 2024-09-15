package com.liro.consultations.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import com.liro.consultations.model.enums.ConsultationType;

import java.time.LocalDate;
import java.util.Date;

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
    private LocalDate localDate;
}
