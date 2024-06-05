package com.liro.consultations.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import com.liro.consultations.dtos.ConsultationDTO;
import com.liro.consultations.model.enums.ConsultationType;


@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ConsultationResponse extends ConsultationDTO {

    private Long id;
}
