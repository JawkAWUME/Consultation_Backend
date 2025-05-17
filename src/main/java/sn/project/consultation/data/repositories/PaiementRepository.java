package sn.project.consultation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sn.project.consultation.data.entities.Paiement;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    Optional<Paiement> findTopByPatientIdOrderByDatePaiementDesc(Long patientId);

    @Query("SELECT COUNT(p) FROM Paiement p WHERE p.patient.id = :patientId AND p.statut = 'ECHEC' AND p.datePaiement >= :since")
    long countFailuresRecentes(@Param("patientId") Long patientId, @Param("since") LocalDateTime since);
}
