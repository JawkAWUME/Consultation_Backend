package sn.project.consultation.services;

import sn.project.consultation.api.dto.ProSanteDTO;
import sn.project.consultation.api.dto.RechercheProDTO;
import sn.project.consultation.api.dto.RendezVousDTO;
import sn.project.consultation.api.dto.TourneeOptimiseeDTO;

import java.util.List;

public interface RendezVousService {
    RendezVousDTO creerRendezVous(RendezVousDTO dto);
    RendezVousDTO modifierRendezVous(Long id, RendezVousDTO dto);
    void annulerRendezVous(Long id);
    List<RendezVousDTO> listerRendezVousParPatient(Long patientId);
    List<ProSanteDTO> rechercherProfessionnels(RechercheProDTO criteres);
     TourneeOptimiseeDTO optimiserTournee(Long professionnelId);
}
