package sn.project.consultation.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.project.consultation.api.dto.MessageDTO;
import sn.project.consultation.api.dto.PlanificationDTO;
import sn.project.consultation.data.entities.Message;
import sn.project.consultation.data.entities.Teleconsultation;
import sn.project.consultation.services.impl.TeleconsultationService;

import java.util.List;

@RestController
@RequestMapping("/api/teleconsultations")
public class TeleconsultationController {

    @Autowired
    private TeleconsultationService teleconsultationService;

    /**
     * Planifie une téléconsultation
     */
    @PostMapping("/planifier")
    public ResponseEntity<Teleconsultation> planifier(@RequestBody PlanificationDTO dto) {
        Teleconsultation tc = teleconsultationService.planifierTeleconsultation(dto.getPatientId(), dto.getMedecinId(), dto.getDateHeure());
        return ResponseEntity.ok(tc);
    }

    /**
     * Envoie un message
     */
    @PostMapping("/{id}/messages")
    public ResponseEntity<Message> envoyerMessage(@PathVariable Long id, @RequestBody MessageDTO dto) {
        Message msg = teleconsultationService.envoyerMessage(id, dto.getExpediteurId(), dto.getContenu());
        return ResponseEntity.ok(msg);
    }

    /**
     * Récupère les messages d'une téléconsultation
     */
    @GetMapping("/{id}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long id) {
        List<Message> messages = teleconsultationService.getMessages(id);
        return ResponseEntity.ok(messages);
    }
}
