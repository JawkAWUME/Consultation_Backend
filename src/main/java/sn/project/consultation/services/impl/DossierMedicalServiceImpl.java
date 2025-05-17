package sn.project.consultation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.project.consultation.api.dto.DocumentDTO;
import sn.project.consultation.api.dto.DossierMedicalDTO;
import sn.project.consultation.api.dto.HistoriqueConsultationDTO;
import sn.project.consultation.api.dto.PatientDTO;
import sn.project.consultation.data.entities.DocumentMedical;
import sn.project.consultation.data.entities.DossierMedical;
import sn.project.consultation.data.entities.HistoriqueConsultation;
import sn.project.consultation.data.repositories.DocumentMedicalRepository;
import sn.project.consultation.data.repositories.DossierMedicalRepository;
import sn.project.consultation.services.DossierMedicalService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;




@Service
public class DossierMedicalServiceImpl implements DossierMedicalService {

    @Autowired private DossierMedicalRepository repo;
    @Autowired private DocumentMedicalRepository docRepo;

    private static final Set<String> PATHOLOGIES_CLES = Set.of(
            "diabete", "hypertension", "asthme", "cancer", "allergie", "cholesterol", "cardiaque", "anemie", "ulcere", "covid"
    );

    /**
     * ✅ Récupération complète et enrichie du dossier
     */
    public DossierMedicalDTO getDossierByPatientId(Long id) {
        DossierMedical dossier = repo.findByPatientId(id).orElseThrow();
        DossierMedicalDTO dto = mapToDTO(dossier);

        // Générer résumé si manquant
        if (dossier.getResume() == null || dossier.getResume().isBlank()) {
            dto.setResume(genererResumeIntelligent(dossier));
        }

        return dto;
    }

    /**
     * ✅ Ajout d’un document avec structuration intelligente
     */
    public void ajouterDocument(Long dossierId, DocumentDTO doc) {
        DossierMedical dossier = repo.findById(dossierId).orElseThrow();

        DocumentMedical document = new DocumentMedical();
        document.setNom(formaterNomDocument(doc.getNom()));
        document.setUrlStockage(doc.getUrlStockage());
        document.setDossier(dossier);

        dossier.getDocuments().add(document);
        repo.save(dossier);
    }

    /**
     * ✅ Ajout de consultation avec détection intelligente
     */
    public void ajouterHistorique(Long dossierId, HistoriqueConsultationDTO h) {
        DossierMedical dossier = repo.findById(dossierId).orElseThrow();

        HistoriqueConsultation hist = new HistoriqueConsultation();
        hist.setDate(h.getDate());
        hist.setNotes(h.getNotes());
        hist.setTraitement(h.getTraitement());
        hist.setDossier(dossier);

        // 🚨 Détection de mots-clés critiques
        String notesLower = Optional.ofNullable(h.getNotes()).orElse("").toLowerCase();
        if (notesLower.contains("hypertension") || notesLower.contains("urgence")) {
            dossier.setResume("⚠️ Surveillance recommandée : antécédents critiques détectés.");
        }

        dossier.getHistoriques().add(hist);
        repo.save(dossier);
    }

    /**
     * 🔍 Analyse intelligente des pathologies fréquentes
     */
    public Map<String, Long> analyserPathologies(Long patientId) {
        DossierMedical dossier = repo.findByPatientId(patientId).orElseThrow();

        return dossier.getHistoriques().stream()
                .map(HistoriqueConsultation::getNotes)
                .filter(Objects::nonNull)
                .flatMap(notes -> Arrays.stream(notes.toLowerCase().split("[ ,;:.!?]")))
                .filter(mot -> PATHOLOGIES_CLES.contains(mot))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * ✅ Mapping enrichi vers DTO
     */
    private DossierMedicalDTO mapToDTO(DossierMedical d) {
        DossierMedicalDTO dto = new DossierMedicalDTO();
        dto.setId(d.getId());
        dto.setResume(d.getResume());
        dto.setPatient(PatientDTO.fromEntity(d.getPatient()));
        dto.setDocuments(DocumentDTO.fromEntities(docRepo.findByDossierId(d.getId())));

        dto.setHistoriques(
                d.getHistoriques().stream().map(h -> {
                    HistoriqueConsultationDTO hDto = new HistoriqueConsultationDTO();
                    hDto.setDate(h.getDate());
                    hDto.setNotes(h.getNotes());
                    hDto.setTraitement(h.getTraitement());
                    return hDto;
                }).collect(Collectors.toList())
        );

        return dto;
    }

    /**
     * 🧠 Génère un résumé en analysant les pathologies dominantes
     */
    private String genererResumeIntelligent(DossierMedical dossier) {
        Map<String, Long> stats = analyserPathologies(dossier.getPatient().getId());
        return stats.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .map(e -> "📌 " + e.getKey() + " (" + e.getValue() + " occurrence" + (e.getValue() > 1 ? "s" : "") + ")")
                .collect(Collectors.joining(" | ", "Résumé santé : ", ""));
    }

    /**
     * 🗂️ Format intelligent du nom de document
     */
    private String formaterNomDocument(String nom) {
        if (nom == null) return "Document Médical";
        return nom.trim().replaceAll("\\s+", "_").toUpperCase();
    }
}
