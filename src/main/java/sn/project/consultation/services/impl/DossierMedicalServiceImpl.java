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

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DossierMedicalServiceImpl implements DossierMedicalService {

    @Autowired private DossierMedicalRepository repo;
    @Autowired private DocumentMedicalRepository docRepo;

    /**
     * ✅ Récupération complète du dossier médical avec résumé et analyse
     */
    public DossierMedicalDTO getDossierByPatientId(Long id) {
        DossierMedical dossier = repo.findByPatientId(id).orElseThrow();
        return mapToDTO(dossier);
    }

    /**
     * ✅ Ajout d'un document médical
     */
    public void ajouterDocument(Long dossierId, DocumentDTO doc) {
        DossierMedical dossier = repo.findById(dossierId).orElseThrow();
        DocumentMedical document = new DocumentMedical();
        document.setNom(doc.getNom());
        document.setUrlStockage(doc.getUrlStockage());
        document.setDossier(dossier);

        dossier.getDocuments().add(document);
        repo.save(dossier);
    }

    /**
     * ✅ Ajout d’un historique de consultation avec analyse automatique du risque
     */
    public void ajouterHistorique(Long dossierId, HistoriqueConsultationDTO h) {
        DossierMedical dossier = repo.findById(dossierId).orElseThrow();

        HistoriqueConsultation hist = new HistoriqueConsultation();
        hist.setDate(h.getDate());
        hist.setNotes(h.getNotes());
        hist.setTraitement(h.getTraitement());
        hist.setDossier(dossier);

        dossier.getHistoriques().add(hist);

        // Analyse automatique
        if (h.getNotes() != null && h.getNotes().toLowerCase().contains("hypertension")) {
            dossier.setResume("⚠️ Suivi régulier nécessaire : Antécédents d'hypertension.");
        }

        repo.save(dossier);
    }

    /**
     * 🔍 Méthode d’analyse de pathologies fréquentes à partir de l’historique
     */
    public Map<String, Long> analyserPathologies(Long patientId) {
        DossierMedical dossier = repo.findByPatientId(patientId).orElseThrow();

        return dossier.getHistoriques().stream()
                .map(HistoriqueConsultation::getNotes)
                .filter(Objects::nonNull)
                .flatMap(notes -> Arrays.stream(notes.toLowerCase().split("[ ,;.]")))
                .filter(word -> word.length() > 4) // éviter les mots trop courts
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * ✅ Mapping vers DTO enrichi
     */
    private DossierMedicalDTO mapToDTO(DossierMedical d) {
        DossierMedicalDTO dto = new DossierMedicalDTO();
        dto.setId(d.getId());
        dto.setResume(d.getResume());
        dto.setPatient(PatientDTO.fromEntity(d.getPatient()));

        dto.setDocuments(
                DocumentDTO.fromEntities(docRepo.findByDossierId(d.getId()))
        );

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
}

