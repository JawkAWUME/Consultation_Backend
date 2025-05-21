package sn.project.consultation.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.api.dto.DocumentDTO;
import sn.project.consultation.api.dto.DossierMedicalDTO;
import sn.project.consultation.api.dto.HistoriqueConsultationDTO;
import sn.project.consultation.services.DossierMedicalService;

import java.util.Map;

@RestController
@RequestMapping("/api/dossier")
@Tag(name="Dossier Médical", description = "Gestion des dossiers médicaux")
public class DossierMedicalController {
    @Autowired
    DossierMedicalService service;

    // ✅ Récupération du dossier médical complet d’un patient
    @Operation(summary = "Récupérer le dossier médical d'un patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dossier médical récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Patient Non Trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur Serveur"),
    })
    @GetMapping("/patient/{id}")
    public ResponseEntity<DossierMedicalDTO> getDossier(@PathVariable Long id) {
        return ResponseEntity.ok(service.getDossierByPatientId(id));
    }

    // ✅ Ajout d’un document médical (ordonnance, analyse…)
    @Operation(summary = "Ajouter un document médical")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document ajouté avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Patient Non Trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur Serveur"),
    })
    @PostMapping("/{id}/document")
    public ResponseEntity<String> ajouterDocument(@PathVariable Long id, @RequestBody DocumentDTO doc) {
        service.ajouterDocument(id, doc);
        return ResponseEntity.ok("Ajout réussi");
    }

    // ✅ Ajout d’un historique de consultation
    @Operation(summary = "Ajouter un historique de consultation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historique ajouté avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Patient Non Trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur Serveur"),
    })
    @PostMapping("/{id}/historique")
    public ResponseEntity<Void> ajouterHistorique(@PathVariable Long id, @RequestBody HistoriqueConsultationDTO histo) {
        service.ajouterHistorique(id, histo);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Analyser les pathologies les plus fréquentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document ajouté avec succès"),
            @ApiResponse(responseCode = "204", description = "Aucune donnée trouvée"),
            @ApiResponse(responseCode = "404", description = "Patient Non Trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur Serveur"),
    })
    @GetMapping("/diagnostic/{patientId}")
    public ResponseEntity<Map<String, Long>> analyserPathologies(@PathVariable Long patientId) {
        Map<String, Long> resultats = service.analyserPathologies(patientId);
        return resultats.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(resultats);
    }
}