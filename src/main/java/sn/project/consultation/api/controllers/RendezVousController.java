package sn.project.consultation.api.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.api.dto.ProSanteDTO;
import sn.project.consultation.api.dto.RechercheProDTO;
import sn.project.consultation.api.dto.RendezVousDTO;
import sn.project.consultation.api.dto.TourneeOptimiseeDTO;
import sn.project.consultation.services.RendezVousService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/rendezvous")
public class RendezVousController {
    @Autowired
    private RendezVousService service;

    @PostMapping
    public ResponseEntity<RendezVousDTO> creer(@RequestBody RendezVousDTO dto) {
        RendezVousDTO result = service.creerRendezVous(dto);
        if (result.getId() != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> annuler(@PathVariable Long id) {
        try {
            service.annulerRendezVous(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<RendezVousDTO>> lister(@PathVariable Long id) {
        List<RendezVousDTO> liste = service.listerRendezVousParPatient(id);
        if (liste.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(liste);
        }
    }

    // ✅ Modification d’un rendez-vous
    @PutMapping("/{id}")
    public ResponseEntity<RendezVousDTO> modifier(@PathVariable Long id, @RequestBody RendezVousDTO dto) {
        try {
            RendezVousDTO updated = service.modifierRendezVous(id, dto);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/recherche")
    public ResponseEntity<List<ProSanteDTO>> rechercher(@RequestBody RechercheProDTO criteres) {
        List<ProSanteDTO> resultats = service.rechercherProfessionnels(criteres);
        if (resultats.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(resultats);
        }
    }

    @PostMapping("/test-rappel")
    public ResponseEntity<String> testerRappel() {
        service.envoyerRappels();
        return ResponseEntity.ok("Rappels envoyés !");
    }

    @GetMapping("/optimiser-tournee/{id}")
    public ResponseEntity<TourneeOptimiseeDTO> optimiserTournee(@PathVariable Long id) {
        TourneeOptimiseeDTO tournee = service.optimiserTournee(id);
        return ResponseEntity.ok(tournee);
    }

    @GetMapping("/statistiques/{proId}")
    public ResponseEntity<Map<String, Object>> statistiques(@PathVariable Long proId) {
        return ResponseEntity.ok(service.statistiquesHebdo(proId));
    }

    // ✅ Créneaux disponibles pour un professionnel à une date donnée
    @GetMapping("/creneaux-disponibles")
    public ResponseEntity<List<LocalDateTime>> getCreneauxDisponibles(
            @RequestParam Long proId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.getCreneauxDisponibles(proId, date));
    }

    // ✅ Carte des patients pour un professionnel
    @GetMapping("/carte-patients/{proId}")
    public ResponseEntity<List<Map<String, Object>>> getCartePatients(@PathVariable Long proId) {
        return ResponseEntity.ok(service.getCartePatients(proId));
    }
}