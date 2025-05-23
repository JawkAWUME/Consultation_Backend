package sn.project.consultation.services;

import sn.project.consultation.api.dto.DocumentDTO;
import sn.project.consultation.api.dto.DossierMedicalDTO;
import sn.project.consultation.api.dto.FichierMedicalDTO;
import sn.project.consultation.api.dto.HistoriqueConsultationDTO;
import sn.project.consultation.data.entities.*;

import java.util.Map;

public interface DossierMedicalService {

    // === CRUD GLOBAL ===
    DossierMedicalDTO creerDossier(Long patientId);
    DossierMedicalDTO getDossierByPatientId(Long id);

    // === DOCUMENTS MÉDICAUX ===
    void ajouterDocument(Long dossierId, DocumentDTO dto);
    void ajouterFichierAnnexe(Long dossierId, FichierMedicalDTO dto);

    // === HISTORIQUE DE CONSULTATIONS ===
    void ajouterHistorique(Long dossierId, HistoriqueConsultationDTO dto);

    // === ANTÉCÉDENTS MÉDICAUX ===
    void enregistrerAntecedents(Long dossierId, Antecedents antecedents);

    // === EXAMEN CLINIQUE ===
    void enregistrerExamenClinique(Long dossierId, ExamenClinique examen);

    // === EXAMENS COMPLÉMENTAIRES ===
    void enregistrerExamensComplementaires(Long dossierId, ExamensComplementaires examens);

    // === DIAGNOSTIC MÉDICAL ===
    void enregistrerDiagnostic(Long dossierId, DiagnosticMedical diagnostic);

    // === TRAITEMENTS ET PRESCRIPTIONS ===
    void enregistrerTraitements(Long dossierId, TraitementPrescription traitements);

    // === ÉVOLUTION ET SUIVI ===
    void enregistrerEvolutionSuivi(Long dossierId, EvolutionSuivi suivi);

    // === CORRESPONDANCES MÉDICALES ===
    void enregistrerCorrespondances(Long dossierId, Correspondances correspondances);
}
