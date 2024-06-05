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

    private String details;

    @Column(nullable = false)
    private LocalDate localDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultationType consultationType;

    @Column(nullable = false)
    private Long animalId;

    private Long petClinicBranchId;
}