package com.liro.consultations.model.dbentities;

import lombok.*;
import com.liro.consultations.model.enums.ConsultationType;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "consultations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String amnsesis;
    private String clinicalExamination;
    private String presumptiveDiagnosis;
    private String treatment;
    private Double weight;
    private Double temperature;

    @Column(nullable = false)
    private LocalDate localDate;

    @Column(nullable = false)
    private Long animalId;
}