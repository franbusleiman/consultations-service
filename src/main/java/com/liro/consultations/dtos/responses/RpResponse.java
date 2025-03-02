package com.liro.consultations.dtos.responses;

import com.liro.consultations.dtos.ConsultationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class RpResponse  {

    private Long id;
    private Long animalId;
    private Long clinicId;
    private LocalDate control;
}
