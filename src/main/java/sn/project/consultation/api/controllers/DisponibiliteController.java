package sn.project.consultation.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.api.dto.DisponibiliteDTO;
import sn.project.consultation.services.DisponibiliteService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilites")
public class DisponibiliteController {
    @Autowired
    DisponibiliteService dispoService;

    // ✅ Visualisation des disponibilités en temps réel
    @GetMapping("/professionnel/{id}/{date}")
    public ResponseEntity<List<DisponibiliteDTO>> getDisponibilites(@PathVariable Long id, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<DisponibiliteDTO> dispo = dispoService.getDisponibilites(id, date);
        if (dispo.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(dispo);
        }
    }

    // ✅ Mise à jour de disponibilité (annulation/réouverture d’un créneau)
    @PatchMapping("/{id}")
    public ResponseEntity<Void> modifierDispo(@PathVariable Long id, @RequestParam boolean disponible) {
        try {
            dispoService.majDisponibilite(id, disponible);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
