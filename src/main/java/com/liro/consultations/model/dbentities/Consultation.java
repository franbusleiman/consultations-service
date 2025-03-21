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
    private String details;
    private Long vetUserId;
    private Long vetClinicId;

    @Column(nullable = false)
    private LocalDate localDate;
    @OneToOne(mappedBy = "consultation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Rp rp;
    @Column(nullable = false)
    private Long animalId;
}