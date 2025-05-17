package sn.project.consultation.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.api.dto.PaiementRequestDTO;
import sn.project.consultation.data.entities.Facture;
import sn.project.consultation.data.entities.Paiement;
import sn.project.consultation.services.FactureService;
import sn.project.consultation.services.PaiementService;
import sn.project.consultation.data.repositories.PaiementRepository;
import sn.project.consultation.data.repositories.FactureRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/paiements")
public class PaiementController {

    @Autowired private PaiementService paiementService;
    @Autowired private FactureService factureService;
    @Autowired private PaiementRepository paiementRepo;
    @Autowired private FactureRepository factureRepo;

    /**
     * ✅ Effectuer un paiement intelligent avec génération de facture et envoi multi-canal
     */
    @PostMapping("/payer")
    public ResponseEntity<Facture> effectuerPaiement(@RequestBody PaiementRequestDTO dto) {
        Facture facture = paiementService.effectuerPaiement(dto);
        return ResponseEntity.ok(facture);
    }

    /**
     * 📥 Télécharger un reçu PDF d’une facture
     */
//    @GetMapping("/factures/pdf/{factureId}")
//    public ResponseEntity<byte[]> telechargerFacturePDF(@PathVariable Long factureId) {
//        byte[] contenu = factureService.getPdfFacture(factureId);
//        return ResponseEntity
//                .ok()
//                .header("Content-Type", "application/pdf")
//                .header("Content-Disposition", "inline; filename=facture-" + factureId + ".pdf")
//                .body(contenu);
//    }
//
//    /**
//     * 📊 Obtenir les statistiques de paiements d’un patient
//     */
//    @GetMapping("/stats/{patientId}")
//    public ResponseEntity<Map<String, Object>> getStatistiques(@PathVariable Long patientId) {
//        Map<String, Object> stats = paiementService.getStatistiquesPaiements(patientId);
//        return ResponseEntity.ok(stats);
//    }

    /**
     * 📁 Liste des factures d’un patient
     */
    @GetMapping("/factures/{patientId}")
    public ResponseEntity<List<Facture>> listerFactures(@PathVariable Long patientId) {
        List<Facture> factures = factureService.getFacturesByPatient(patientId);
        return ResponseEntity.ok(factures);
    }

    /**
     * 🔍 Détail complet d’un paiement
     */
    @GetMapping("/details/{paiementId}")
    public ResponseEntity<Paiement> getDetailPaiement(@PathVariable Long paiementId) {
        Paiement paiement = paiementRepo.findById(paiementId).orElseThrow();
        return ResponseEntity.ok(paiement);
    }

    /**
     * 🗑️ Supprimer une facture (optionnel - réservé à un rôle admin ou gestionnaire)
     */
    @DeleteMapping("/factures/{id}")
    public ResponseEntity<Void> supprimerFacture(@PathVariable Long id) {
        factureRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
