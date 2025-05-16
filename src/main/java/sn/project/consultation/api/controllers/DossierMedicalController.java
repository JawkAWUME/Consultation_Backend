package sn.project.consultation.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.api.dto.DocumentDTO;
import sn.project.consultation.api.dto.DossierMedicalDTO;
import sn.project.consultation.api.dto.HistoriqueConsultationDTO;
import sn.project.consultation.services.DossierMedicalService;

@RestController
@RequestMapping("/api/dossier")
public class DossierMedicalController {
    @Autowired
    DossierMedicalService service;

    // ✅ Récupération du dossier médical complet d’un patient
    @GetMapping("/patient/{id}")
    public ResponseEntity<DossierMedicalDTO> getDossier(@PathVariable Long id) {
        return ResponseEntity.ok(service.getDossierByPatientId(id));
    }

    // ✅ Ajout d’un document médical (ordonnance, analyse…)
    @PostMapping("/{id}/document")
    public ResponseEntity<Void> ajouterDocument(@PathVariable Long id, @RequestBody DocumentDTO doc) {
        service.ajouterDocument(id, doc);
        return ResponseEntity.ok().build();
    }

    // ✅ Ajout d’un historique de consultation
    @PostMapping("/{id}/historique")
    public ResponseEntity<Void> ajouterHistorique(@PathVariable Long id, @RequestBody HistoriqueConsultationDTO histo) {
        service.ajouterHistorique(id, histo);
        return ResponseEntity.ok().build();
    }
}