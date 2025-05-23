package sn.project.consultation.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.api.dto.MessageDTO;
import sn.project.consultation.api.dto.PlanificationDTO;
import sn.project.consultation.api.dto.TeleconsultationDTO;
import sn.project.consultation.data.entities.Message;
import sn.project.consultation.data.entities.Teleconsultation;
import sn.project.consultation.services.impl.TeleconsultationService;

import java.util.List;

@RestController
@RequestMapping("/api/teleconsultations")
@Tag(name = "Téléconsultation", description = "Gestion des téléconsultations")
public class TeleconsultationController {

    @Autowired
    private TeleconsultationService teleconsultationService;

    /**
     * Planifie une téléconsultation
     */
    @Operation(summary = "Planifier une téléconsultation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Téléconsultation planifiée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides pour la planification"),
            @ApiResponse(responseCode = "404", description = "Patient ou médecin introuvable"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping("/planifier")
    public ResponseEntity<TeleconsultationDTO> planifier(@RequestBody PlanificationDTO dto) {
        Teleconsultation tc = teleconsultationService.planifierTeleconsultation(dto.getPatientId(), dto.getMedecinId(), dto.getDateHeure());
        return ResponseEntity.ok(TeleconsultationDTO.fromEntity(tc));
    }

    /**
     * Envoie un message
     */

    @Operation(summary = "Envoyer un message dans une téléconsultation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message envoyé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données de message invalides"),
            @ApiResponse(responseCode = "404", description = "Téléconsultation ou expéditeur introuvable"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping("/{id}/messages")
    public ResponseEntity<Message> envoyerMessage(@PathVariable Long id, @RequestBody MessageDTO dto) {
        Message msg = teleconsultationService.envoyerMessage(id, dto.getExpediteurId(), dto.getContenu());
        return ResponseEntity.ok(msg);
    }

    /**
     * Récupère les messages d'une téléconsultation
     */

    @Operation(summary = "Récupérer les messages d'une téléconsultation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages récupérés avec succès"),
            @ApiResponse(responseCode = "404", description = "Téléconsultation introuvable"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/{id}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long id) {
        List<Message> messages = teleconsultationService.getMessages(id);
        return ResponseEntity.ok(messages);
    }

}
