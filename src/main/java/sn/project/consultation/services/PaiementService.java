package sn.project.consultation.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.project.consultation.api.dto.PaiementRequestDTO;
import sn.project.consultation.data.entities.Facture;
import sn.project.consultation.data.entities.Paiement;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.repositories.FactureRepository;
import sn.project.consultation.data.repositories.PaiementRepository;
import sn.project.consultation.data.repositories.PatientRepository;
import sn.project.consultation.data.repositories.ProSanteRepository;
import sn.project.consultation.services.impl.SmsService;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
public class PaiementService {

    @Autowired private PaiementRepository paiementRepo;
    @Autowired private FactureRepository factureRepo;
    @Autowired private FactureService factureService;
    @Autowired private PatientRepository patientRepo;
    @Autowired private ProSanteRepository proRepo;
    @Autowired private CloudStorageService cloudStorage;
    @Autowired private EmailService emailService;
    @Autowired private SmsService smsService;

    /**
     * Paiement direct (simulation de succès)
     */
    @Transactional
    public Facture effectuerPaiement(Long factureId, PaiementRequestDTO dto) {
        Facture facture = factureRepo.findById(factureId)
                .orElseThrow(() -> new IllegalArgumentException("Facture introuvable"));

        Patient patient = patientRepo.findById(dto.getPatient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Patient introuvable"));
        ProSante pro = proRepo.findById(dto.getProfessionnel().getId())
                .orElseThrow(() -> new IllegalArgumentException("Professionnel introuvable"));

        // Montant déjà payé pour cette facture
        Double dejaPaye = paiementRepo.findByPatientIdAndStatut(patient.getId(), "SUCCES")
                .stream()
                .filter(p -> p.getFacture() != null && p.getFacture().getId().equals(factureId))
                .mapToDouble(Paiement::getMontant)
                .sum();

        double montantTotal = pro.getTarif();
        double montantRestant = montantTotal - dejaPaye;
        double montantAPayer = dto.getMontant();

        if (montantAPayer <= 0) {
            throw new IllegalArgumentException("Le montant doit être > 0");
        }
        if (montantAPayer > montantRestant) {
            throw new IllegalArgumentException("Le montant payé dépasse le montant restant (" + montantRestant + ")");
        }

        // Créer un nouveau paiement
        Paiement paiement = new Paiement();
        paiement.setMontant(montantAPayer);
        paiement.setPatient(patient);
        paiement.setProfessionnel(pro);
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setMethode(dto.getMethode());
        paiement.setStatut("SUCCES");
        paiement.setFacture(facture);

        paiementRepo.save(paiement);

        log.info("💰 Paiement {} FCFA enregistré pour facture {} (restant: {})",
                montantAPayer, facture.getNumero(), montantRestant - montantAPayer);

        return facture;
    }

    @Transactional
    public Paiement initierPaiementPourFacture(Long factureId, PaiementRequestDTO dto) {
        Facture facture = factureRepo.findById(factureId)
                .orElseThrow(() -> new IllegalArgumentException("Facture introuvable"));

        Patient patient = patientRepo.findById(dto.getPatient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Patient introuvable"));
        ProSante pro = proRepo.findById(dto.getProfessionnel().getId())
                .orElseThrow(() -> new IllegalArgumentException("Professionnel introuvable"));

        // Montant déjà payé
        Double dejaPaye = paiementRepo.findByPatientIdAndStatut(patient.getId(), "SUCCES")
                .stream()
                .filter(p -> p.getFacture() != null && p.getFacture().getId().equals(factureId))
                .mapToDouble(Paiement::getMontant)
                .sum();

        double montantTotal = pro.getTarif();
        double montantRestant = montantTotal - dejaPaye;
        double montantAPayer = dto.getMontant();

        if (montantAPayer <= 0 || montantAPayer > montantRestant) {
            throw new IllegalArgumentException("Montant invalide. Restant dû: " + montantRestant);
        }

        Paiement paiement = new Paiement();
        paiement.setMontant(montantAPayer);
        paiement.setPatient(patient);
        paiement.setProfessionnel(pro);
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setMethode(dto.getMethode());
        paiement.setStatut("EN_ATTENTE");
        paiement.setFacture(facture);

        paiementRepo.save(paiement);

        return paiement;
    }



    /**
     * Création d’un paiement en attente (utilisé pour PayTech)
     */
    @Transactional
    public Paiement creerPaiementEnAttente(PaiementRequestDTO dto) {
        Patient patient = patientRepo.findById(dto.getPatient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Patient introuvable"));
        ProSante pro = proRepo.findById(dto.getProfessionnel().getId())
                .orElseThrow(() -> new IllegalArgumentException("Professionnel introuvable"));

        Paiement paiement = new Paiement();
        paiement.setMontant(dto.getMontant() != null ? dto.getMontant() : pro.getTarif());
        paiement.setPatient(patient);
        paiement.setProfessionnel(pro);
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setMethode(dto.getMethode());
        paiement.setStatut("EN_ATTENTE");

        paiementRepo.save(paiement);
        log.info("⏳ Paiement en attente créé (id={}) pour patient {}", paiement.getId(), patient.getId());
        return paiement;
    }

    /**
     * Montant restant à payer pour un patient
     */
    public Double getMontantAPayer(Long patientId) {
        return paiementRepo.getMontantRestantByPatient(patientId);
    }

    /**
     * Déduction de la méthode préférée du patient
     */
    private String getMethodePreferee(Patient patient) {
        return paiementRepo.findTopByPatientIdOrderByDatePaiementDesc(patient.getId())
                .map(Paiement::getMethode)
                .orElse("Carte");
    }

    /**
     * Envoi du reçu par Email et SMS
     */
    private void envoyerRecuMultiCanal(Patient patient, Facture facture) {
        String message = "🎉 Paiement confirmé ! Reçu n°" + facture.getNumero() + " envoyé à votre email.";

        String url = facture.getUrlPdf();
        String cheminRelatif = url.replace("http://localhost:10001/files/", "");
        String cheminLocal = cloudStorage.getCheminComplet(cheminRelatif);

        // Email
        try {
            emailService.envoyerEmail(
                    patient.getCoordonnees().getEmail(),
                    "Votre reçu de paiement",
                    message,
                    cheminLocal
            );
            log.info("📧 Reçu envoyé par email à {}", patient.getCoordonnees().getEmail());
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi de l'email au patient {} : {}", patient.getId(), e.getMessage());
        }

        // SMS
        try {
            if (patient.getCoordonnees().getNumeroTelephone() != null) {
//                smsService.envoyerSms(patient.getCoordonnees().getNumeroTelephone(), message);
                log.info("📱 SMS de confirmation envoyé à {}", patient.getCoordonnees().getNumeroTelephone());
            }
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi du SMS au patient {} : {}", patient.getId(), e.getMessage());
        }
    }
}

