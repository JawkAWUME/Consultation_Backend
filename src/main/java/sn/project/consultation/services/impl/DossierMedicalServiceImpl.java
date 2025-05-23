package sn.project.consultation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.project.consultation.api.dto.*;
import sn.project.consultation.data.entities.*;
import sn.project.consultation.data.repositories.*;
import sn.project.consultation.services.DossierMedicalService;


@Service
public class DossierMedicalServiceImpl implements DossierMedicalService {

    @Autowired private DossierMedicalRepository dossierRepo;
    @Autowired private PatientRepository patientRepo;
    @Autowired private DocumentMedicalRepository documentRepo;
    @Autowired private FichierMedicalRepository fichierRepo;
    @Autowired private HistoriqueConsultationRepository historiqueRepo;

    // === CRUD GLOBAL ===

    public DossierMedicalDTO creerDossier(Long patientId) {
        Patient patient = patientRepo.findById(patientId).orElseThrow(() -> new RuntimeException("Patient non trouvé"));
        DossierMedical dossier = new DossierMedical();
        dossier.setPatient(patient);
        dossier = dossierRepo.save(dossier);
        return DossierMedicalDTO.fromEntity(dossier);
    }

    public DossierMedicalDTO getDossierByPatientId(Long id) {
        return DossierMedicalDTO.fromEntity(
                dossierRepo.findByPatientId(id).orElseThrow(() -> new RuntimeException("Dossier introuvable")));
    }

    private DossierMedical getDossier(Long id) {
        return dossierRepo.findById(id).orElseThrow(() -> new RuntimeException("Dossier introuvable"));
    }

    // === DOCUMENTS MÉDICAUX ===

    public void ajouterDocument(Long dossierId, DocumentDTO dto) {
        DossierMedical dossier = getDossier(dossierId);
        DocumentMedical doc = DocumentDTO.toEntity(dto);
        dossier.getDocuments().add(doc);
        documentRepo.save(doc);
    }

    public void ajouterFichierAnnexe(Long dossierId, FichierMedicalDTO dto) {
        DossierMedical dossier = getDossier(dossierId);
        FichierMedical fichier = FichierMedicalDTO.toEntity(dto, dossier);
        fichierRepo.save(fichier);
    }

    // === HISTORIQUE DE CONSULTATIONS ===

    public void ajouterHistorique(Long dossierId, HistoriqueConsultationDTO dto) {
        DossierMedical dossier = getDossier(dossierId);
        HistoriqueConsultation histo = HistoriqueConsultationDTO.toEntity(dto);
        dossier.getHistoriques().add(histo);
        historiqueRepo.save(histo);
    }

    // === ANTÉCÉDENTS MÉDICAUX ===

    public void enregistrerAntecedents(Long dossierId, Antecedents antecedents) {
        DossierMedical dossier = getDossier(dossierId);
        dossier.setAntecedents(antecedents);
        dossierRepo.save(dossier);
    }

    // === EXAMEN CLINIQUE ===

    public void enregistrerExamenClinique(Long dossierId, ExamenClinique examen) {
        DossierMedical dossier = getDossier(dossierId);
        dossier.setExamenClinique(examen);
        dossierRepo.save(dossier);
    }

    // === EXAMENS COMPLÉMENTAIRES ===

    public void enregistrerExamensComplementaires(Long dossierId, ExamensComplementaires examens) {
        DossierMedical dossier = getDossier(dossierId);
        dossier.setExamensComplementaires(examens);
        dossierRepo.save(dossier);
    }

    // === DIAGNOSTIC MÉDICAL ===

    public void enregistrerDiagnostic(Long dossierId, DiagnosticMedical diagnostic) {
        DossierMedical dossier = getDossier(dossierId);
        dossier.setDiagnosticMedical(diagnostic);
        dossierRepo.save(dossier);
    }

    // === TRAITEMENTS ET PRESCRIPTIONS ===

    public void enregistrerTraitements(Long dossierId, TraitementPrescription traitements) {
        DossierMedical dossier = getDossier(dossierId);
        dossier.setTraitements(traitements);
        dossierRepo.save(dossier);
    }

    // === ÉVOLUTION ET SUIVI ===

    public void enregistrerEvolutionSuivi(Long dossierId, EvolutionSuivi suivi) {
        DossierMedical dossier = getDossier(dossierId);
        dossier.setEvolutionSuivi(suivi);
        dossierRepo.save(dossier);
    }

    // === CORRESPONDANCES MÉDICALES ===

    public void enregistrerCorrespondances(Long dossierId, Correspondances correspondances) {
        DossierMedical dossier = getDossier(dossierId);
        dossier.setCorrespondances(correspondances);
        dossierRepo.save(dossier);
    }
}

