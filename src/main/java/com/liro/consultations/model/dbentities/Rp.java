package com.liro.consultations.model.dbentities;

import lombok.*;
import com.liro.consultations.model.enums.ConsultationType;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "rp_treatments", joinColumns = @JoinColumn(name = "rp_id"))
    @Column(name = "treatment")
    private List<String> treatments;

    private String needToObserve;

    private LocalDateTime control;

    @OneToOne
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;
}