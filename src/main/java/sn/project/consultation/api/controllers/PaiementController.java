package sn.project.consultation.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.api.dto.PaiementRequestDTO;
import sn.project.consultation.api.dto.PatientDTO;
import sn.project.consultation.api.dto.ProSanteDTO;
import sn.project.consultation.data.entities.Facture;
import sn.project.consultation.data.entities.Paiement;
import sn.project.consultation.services.FactureService;
import sn.project.consultation.services.PaiementService;
import sn.project.consultation.data.repositories.PaiementRepository;
import sn.project.consultation.data.repositories.FactureRepository;
import sn.project.consultation.services.impl.PaytechService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/paiements")
@Tag(name = "Paiements", description = "Paiement, Facture")
public class PaiementController {

    @Autowired private PaiementService paiementService;
    @Autowired private FactureService factureService;
    @Autowired private PaiementRepository paiementRepo;
    @Autowired private FactureRepository factureRepo;
    @Autowired private PaytechService paytechService; // Service que nous allons utiliser

    // -------------------------
    // Paiement intelligent existant
    // -------------------------
    @PostMapping("/payer")
    public ResponseEntity<Facture> effectuerPaiement(@RequestBody PaiementRequestDTO dto) {
        Facture facture = paiementService.effectuerPaiement(dto);
        return ResponseEntity.ok(facture);
    }

    // -------------------------
    // Nouveau : Initier un paiement via PayTech
    // -------------------------
    @PostMapping("/initier")
    @Operation(summary = "Initier un paiement via Wave ou Orange Money")
    public ResponseEntity<Map<String, String>> initierPaiement(@RequestBody PaiementRequestDTO dto) {
        // Crée le paiement en base
        Paiement paiement = new Paiement();
        paiement.setMontant(dto.getMontant());
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setPatient(PatientDTO.toEntity(dto.getPatient()));
        paiement.setProfessionnel(ProSanteDTO.toEntity(dto.getProfessionnel()));
        paiement.setMethode(dto.getMethode()); // "WAVE" ou "ORANGE"
        dto.setStatut("EN_ATTENTE");
        paiement.setStatut(dto.getStatut());

        paiementRepo.save(paiement);

        // Génère le lien de paiement via PayTech
        String urlPaiement = paytechService.initierPaiement(
                paiement.getMontant(),
                paiement.getMethode(),
                paiement.getId()
        );

        // Retourne l'URL au frontend
        Map<String, String> response = new HashMap<>();
        response.put("paiementUrl", urlPaiement);
        response.put("paiementId", paiement.getId().toString());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Montant restant à payer d’un patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Montant récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/montant/{patientId}")
    public ResponseEntity<Double> getMontantAPayer(@PathVariable Long patientId) {
        Double montant = paiementService.getMontantAPayer(patientId);
        return ResponseEntity.ok(montant);
    }

    // -------------------------
    // Détail paiement existant
    // -------------------------
    @GetMapping("/details/{paiementId}")
    public ResponseEntity<Paiement> getDetailPaiement(@PathVariable Long paiementId) {
        Paiement paiement = paiementRepo.findById(paiementId).orElseThrow();
        return ResponseEntity.ok(paiement);
    }

    // -------------------------
    // Liste des factures existantes
    // -------------------------
    @GetMapping("/factures/{patientId}")
    public ResponseEntity<List<Facture>> listerFactures(@PathVariable Long patientId) {
        List<Facture> factures = factureService.getFacturesByPatient(patientId);
        return ResponseEntity.ok(factures);
    }

    // -------------------------
    // Supprimer une facture existante
    // -------------------------
    @DeleteMapping("/factures/{id}")
    public ResponseEntity<Void> supprimerFacture(@PathVariable Long id) {
        factureRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }



}
