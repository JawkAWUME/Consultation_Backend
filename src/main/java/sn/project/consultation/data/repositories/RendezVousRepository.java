package sn.project.consultation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.project.consultation.data.entities.RendezVous;

import java.time.LocalDateTime;
import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    List<RendezVous> findByPatientId(Long patientId);
    List<RendezVous> findByProsanteIdAndDateHeureAfterOrderByDateHeureAsc(Long professionnelId, LocalDateTime dateHeure);
    List<RendezVous> findByDateHeureBetween(LocalDateTime start, LocalDateTime end);
    List<RendezVous> findByDateHeureBefore(LocalDateTime dateTime);
}