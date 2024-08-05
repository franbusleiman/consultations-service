package com.liro.consultations.repositories;

import com.liro.consultations.model.dbentities.Consultation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    Page<Consultation> findAllByAnimalId(Long animalId, Pageable pageable );

    Long countByAnimalIdAndVetUserId(Long animalId, Long vetUserId);

    Consultation findTopByAnimalIdAndVetUserIdOrderByPublicationDateDesc(Long animalId, Long vetUserId);
}