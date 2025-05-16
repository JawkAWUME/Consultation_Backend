package sn.project.consultation.services;

import sn.project.consultation.api.dto.ProSanteDTO;
import sn.project.consultation.api.dto.RechercheProDTO;
import sn.project.consultation.api.dto.RendezVousDTO;
import sn.project.consultation.api.dto.TourneeOptimiseeDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface RendezVousService {
    RendezVousDTO creerRendezVous(RendezVousDTO dto);
    RendezVousDTO modifierRendezVous(Long id, RendezVousDTO dto);
    void annulerRendezVous(Long id);
    List<RendezVousDTO> listerRendezVousParPatient(Long patientId);
    List<ProSanteDTO> rechercherProfessionnels(RechercheProDTO criteres);
    TourneeOptimiseeDTO optimiserTournee(Long professionnelId);
    void envoyerRappels();
    List<LocalDateTime> getCreneauxDisponibles(Long proId, LocalDate date);
    Map<String, Object> statistiquesHebdo(Long professionnelId);
    List<Map<String, Object>> getCartePatients(Long proId);
}
