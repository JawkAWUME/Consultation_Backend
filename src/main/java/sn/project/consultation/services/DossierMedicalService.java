package sn.project.consultation.services;

import sn.project.consultation.api.dto.DocumentDTO;
import sn.project.consultation.api.dto.DossierMedicalDTO;
import sn.project.consultation.api.dto.HistoriqueConsultationDTO;

public interface DossierMedicalService {
    DossierMedicalDTO getDossierByPatientId(Long patientId);
    void ajouterDocument(Long dossierId, DocumentDTO doc);
    void ajouterHistorique(Long dossierId, HistoriqueConsultationDTO histo);
}
