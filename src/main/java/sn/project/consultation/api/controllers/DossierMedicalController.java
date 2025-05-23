package sn.project.consultation.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.api.dto.*;
import sn.project.consultation.data.entities.Antecedents;
import sn.project.consultation.data.entities.Correspondances;
import sn.project.consultation.data.entities.EvolutionSuivi;
import sn.project.consultation.services.DossierMedicalService;

import java.util.Map;

@RestController
@RequestMapping("/api/dossier")
@Tag(name="Dossier Médical", description = "Gestion des dossiers médicaux")
public class DossierMedicalController {
    @Autowired
    DossierMedicalService service;

    // ✅ Récupération du dossier médical complet d’un patient
    @Autowired
    private DossierMedicalService dossierService;

    // 📌 Créer un dossier médical pour un patient
    @Operation(summary = "Créer un dossier médical pour un patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dossier créé avec succès"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PostMapping("/creer/{patientId}")
    public ResponseEntity<DossierMedicalDTO> creerDossier(@PathVariable Long patientId) {
        return ResponseEntity.ok(dossierService.creerDossier(patientId));
    }

    // 📌 Récupérer un dossier complet
    @Operation(summary = "Récupérer le dossier médical complet d’un patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dossier trouvé"),
            @ApiResponse(responseCode = "404", description = "Aucun dossier trouvé")
    })
    @GetMapping("/patient/{id}")
    public ResponseEntity<DossierMedicalDTO> getDossier(@PathVariable Long id) {
        return ResponseEntity.ok(dossierService.getDossierByPatientId(id));
    }

    // 📌 Ajouter un document médical
    @Operation(summary = "Ajouter un document médical (ex : ordonnance, compte-rendu...)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Document ajouté"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PostMapping("/{id}/document")
    public ResponseEntity<Void> ajouterDocument(@PathVariable Long id, @RequestBody DocumentDTO doc) {
        dossierService.ajouterDocument(id, doc);
        return ResponseEntity.ok().build();
    }

    // 📌 Ajouter un fichier annexe (PDF, image médicale...)
    @Operation(summary = "Ajouter un fichier médical annexe (PDF, image...)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fichier ajouté"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PostMapping("/{id}/fichier-annexe")
    public ResponseEntity<Void> ajouterFichier(@PathVariable Long id, @RequestBody FichierMedicalDTO dto) {
        dossierService.ajouterFichierAnnexe(id, dto);
        return ResponseEntity.ok().build();
    }

    // 📌 Ajouter un historique de consultation
    @Operation(summary = "Ajouter une consultation dans l'historique du dossier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historique ajouté"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PostMapping("/{id}/historique")
    public ResponseEntity<Void> ajouterHistorique(@PathVariable Long id, @RequestBody HistoriqueConsultationDTO dto) {
        dossierService.ajouterHistorique(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Mettre à jour les antécédents médicaux d’un patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Antécédents mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PutMapping("/{id}/antecedents")
    public ResponseEntity<Void> updateAntecedents(@PathVariable Long id, @RequestBody AntecedentsDTO dto) {
        dossierService.enregistrerAntecedents(id, AntecedentsDTO.toEntity(dto));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Mettre à jour l'examen clinique d’un patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Examen clinique mis à jour"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PutMapping("/{id}/examen-clinique")
    public ResponseEntity<Void> updateExamenClinique(@PathVariable Long id, @RequestBody ExamenCliniqueDTO dto) {
        dossierService.enregistrerExamenClinique(id, ExamenCliniqueDTO.toEntity(dto));
        return ResponseEntity.ok().build();
    }

//    @Operation(summary = "Mettre à jour les examens complémentaires")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Examens complémentaires mis à jour"),
//            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
//    })
//    @PutMapping("/{id}/examens-complementaires")
//    public ResponseEntity<Void> updateExamensComplementaires(@PathVariable Long id, @RequestBody ExamensComplementairesDTO dto) {
//        dossierService.enregistrerExamensComplementaires(id, ExamensComplementairesDTO.toEntity(dto));
//        return ResponseEntity.ok().build();
//    }

//    @Operation(summary = "Mettre à jour le diagnostic médical")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Diagnostic mis à jour"),
//            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
//    })
//    @PutMapping("/{id}/diagnostic")
//    public ResponseEntity<Void> updateDiagnostic(@PathVariable Long id, @RequestBody DiagnosticMedicalDTO dto) {
//        dossierService.enregistrerDiagnostic(id, DiagnosticMedicalDTO.toEntity(dto));
//        return ResponseEntity.ok().build();
//    }

    @Operation(summary = "Mettre à jour les traitements et prescriptions")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Traitement mis à jour"),
            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
    })
    @PutMapping("/{id}/traitements")
    public ResponseEntity<Void> updateTraitements(@PathVariable Long id, @RequestBody TraitementPrescriptionDTO dto) {
        dossierService.enregistrerTraitements(id, TraitementPrescriptionDTO.toEntity(dto));
        return ResponseEntity.ok().build();
    }

//    @Operation(summary = "Mettre à jour l’évolution et le suivi clinique")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Évolution mise à jour"),
//            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
//    })
//    @PutMapping("/{id}/evolution")
//    public ResponseEntity<Void> updateEvolution(@PathVariable Long id, @RequestBody EvolutionSuivi dto) {
//        dossierService.enregistrerEvolutionSuivi(id, dto);
//        return ResponseEntity.ok().build();
//    }
//
//    @Operation(summary = "Mettre à jour les correspondances médicales")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Correspondances mises à jour"),
//            @ApiResponse(responseCode = "404", description = "Dossier introuvable")
//    })
//    @PutMapping("/{id}/correspondances")
//    public ResponseEntity<Void> updateCorrespondances(@PathVariable Long id, @RequestBody Correspondances dto) {
//        dossierService.enregistrerCorrespondances(id, dto);
//        return ResponseEntity.ok().build();
//    }


}