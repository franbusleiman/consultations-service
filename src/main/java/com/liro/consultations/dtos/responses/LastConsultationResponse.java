package com.liro.consultations.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class LastConsultationResponse {

    private Long totalConsultations;
    private Long lastConsultationInDays;
    private String title;
}
