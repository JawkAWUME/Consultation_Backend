package sn.project.consultation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.project.consultation.data.entities.Message;
import sn.project.consultation.data.entities.Teleconsultation;
import sn.project.consultation.data.entities.User;
import sn.project.consultation.data.repositories.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TeleconsultationService {

    @Autowired
    private TeleconsultationRepository teleconsultationRepo;

    @Autowired
    private UserRepository utilisateurRepo;
    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private ProSanteRepository proSanteRepository;

    /**
     * Planifie une nouvelle téléconsultation
     */
    public Teleconsultation planifierTeleconsultation(Long patientId, Long medecinId, LocalDateTime dateHeure) {
        Teleconsultation tc = new Teleconsultation();
        tc.setPatient(patientRepo.findById(patientId).orElseThrow());
        tc.setProSante(proSanteRepository.findById(medecinId).orElseThrow());
        tc.setDateHeure(dateHeure);
        tc.setStatut("EN_ATTENTE");
        tc.setLienVideo(genererLienVideoSecurise());
        return teleconsultationRepo.save(tc);
    }

    /**
     * Envoie un message dans le cadre d'une téléconsultation
     */
    public Message envoyerMessage(Long teleconsultationId, Long expediteurId, String contenu) {
        Teleconsultation tc = teleconsultationRepo.findById(teleconsultationId).orElseThrow();
        User expediteur = utilisateurRepo.findById(expediteurId).orElseThrow();
        Message msg = new Message();
        msg.setTeleconsultation(tc);
        msg.setExpediteur(expediteur);
        msg.setContenu(contenu);
        msg.setDateEnvoi(LocalDateTime.now());
        return messageRepo.save(msg);
    }

    /**
     * Récupère les messages d'une téléconsultation
     */
    public List<Message> getMessages(Long teleconsultationId) {
        return messageRepo.findByTeleconsultationIdOrderByDateEnvoiAsc(teleconsultationId);
    }

    /**
     * Génère un lien vidéo sécurisé (stub pour exemple)
     */
    private String genererLienVideoSecurise() {
        return "https://meet.jit.si/teleconsultation-" + UUID.randomUUID();
    }

}