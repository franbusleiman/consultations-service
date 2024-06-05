package main.java.com.liro.consultations.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import main.java.com.liro.consultations.model.enums.ConsultationType;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ConsultationDTO {

    private String details;
    private Long animalId;
    private Long petClinicBranchId;
    private ConsultationType consultationType;
}
