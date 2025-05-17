package sn.project.consultation.services;

import sn.project.consultation.api.dto.DocumentDTO;
import sn.project.consultation.api.dto.DossierMedicalDTO;
import sn.project.consultation.api.dto.HistoriqueConsultationDTO;

import java.util.Map;

public interface DossierMedicalService {
    DossierMedicalDTO getDossierByPatientId(Long patientId);
    void ajouterDocument(Long dossierId, DocumentDTO doc);
    void ajouterHistorique(Long dossierId, HistoriqueConsultationDTO histo);
    Map<String, Long> analyserPathologies(Long patientId);
}
