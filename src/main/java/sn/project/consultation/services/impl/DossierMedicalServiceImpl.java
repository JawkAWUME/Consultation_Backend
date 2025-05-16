package sn.project.consultation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.project.consultation.api.dto.DocumentDTO;
import sn.project.consultation.api.dto.DossierMedicalDTO;
import sn.project.consultation.api.dto.HistoriqueConsultationDTO;
import sn.project.consultation.data.entities.DocumentMedical;
import sn.project.consultation.data.entities.DossierMedical;
import sn.project.consultation.data.entities.HistoriqueConsultation;
import sn.project.consultation.data.repositories.DossierMedicalRepository;
import sn.project.consultation.services.DossierMedicalService;

@Service
public class DossierMedicalServiceImpl implements DossierMedicalService {
    @Autowired
    private DossierMedicalRepository repo;

    public DossierMedicalDTO getDossierByPatientId(Long id) {
        DossierMedical dossier = repo.findByPatientId(id).orElseThrow();
        // mapping vers DTO à implémenter
        return mapToDTO(dossier);
    }

    public void ajouterDocument(Long dossierId, DocumentDTO doc) {
        DossierMedical dossier = repo.findById(dossierId).orElseThrow();
        DocumentMedical d = new DocumentMedical();
        d.setNom(doc.getNom());
        d.setUrlStockage(doc.getUrlStockage());
        d.setDossier(dossier);
        dossier.getDocuments().add(d);
        repo.save(dossier);
    }

    public void ajouterHistorique(Long dossierId, HistoriqueConsultationDTO h) {
        DossierMedical dossier = repo.findById(dossierId).orElseThrow();
        HistoriqueConsultation hist = new HistoriqueConsultation();
        hist.setDate(h.getDate());
        hist.setNotes(h.getNotes());
        hist.setTraitement(h.getTraitement());
        hist.setDossier(dossier);
        dossier.getHistoriques().add(hist);
        repo.save(dossier);
    }

    private DossierMedicalDTO mapToDTO(DossierMedical d) {
        // Mapping complet à écrire si besoin
        return new DossierMedicalDTO();
    }
}
