package com.liro.consultations.repositories;

import com.liro.consultations.model.dbentities.Consultation;
import com.liro.consultations.model.dbentities.Rp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RpRepository extends JpaRepository<Rp, Long> {

    @Query("SELECT r FROM Rp r WHERE DATE(r.control) = :nextDay AND r.consultation.animalId IN :animalIds")
    List<Rp> findRpsWithControlNextDay(@Param("nextDay") LocalDateTime nextDay,
                                       @Param("animalIds") List<Long> animalIds);
}
