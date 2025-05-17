package sn.project.consultation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.project.consultation.api.dto.PaiementRequestDTO;
import sn.project.consultation.data.entities.Facture;
import sn.project.consultation.data.entities.Paiement;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.repositories.PaiementRepository;
import sn.project.consultation.data.repositories.PatientRepository;
import sn.project.consultation.data.repositories.ProSanteRepository;
import sn.project.consultation.services.impl.SmsService;

import java.time.LocalDateTime;

@Service
public class PaiementService {

    @Autowired
    private PaiementRepository paiementRepo;
    @Autowired private FactureService factureService;
    @Autowired private PatientRepository patientRepo;
    @Autowired private ProSanteRepository proRepo;
    @Autowired private EmailService emailService;
    @Autowired private SmsService smsService;

    // ✅ Traitement intelligent du paiement
    public Facture effectuerPaiement(PaiementRequestDTO dto) {
        Patient patient = patientRepo.findById(dto.getPatientId()).orElseThrow();
        ProSante pro = proRepo.findById(dto.getProfessionnelId()).orElseThrow();

        // 🔐 Contrôle anti-fraude : 3 paiements échoués en 1h
        if (paiementRepo.countFailuresRecentes(patient.getId(), LocalDateTime.now().minusHours(1)) >= 3) {
            throw new IllegalStateException("Trop de tentatives échouées, veuillez réessayer plus tard.");
        }

        Double montant = pro.getTarif();

        // 💡 Suggestion de méthode basée sur l’historique
        String methodePredefinie = getMethodePreferee(patient);

        Paiement paiement = new Paiement();
        paiement.setMontant(montant);
        paiement.setPatient(patient);
        paiement.setProfessionnel(pro);
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setMethode(dto.getMethode() != null ? dto.getMethode() : methodePredefinie);
        paiement.setStatut("SUCCES"); // Simulé

        // 🤖 Génération automatique de facture
        Facture facture = factureService.genererEtEnvoyerFacture(paiement);
        paiement.setFacture(facture);
        paiementRepo.save(paiement);
        // 📱 Envoi multi-canal du reçu
        envoyerRecuMultiCanal(patient, facture);

        return facture;
    }

    // 🔁 Déduction méthode préférée du patient
    private String getMethodePreferee(Patient patient) {
        return paiementRepo.findTopByPatientIdOrderByDatePaiementDesc(patient.getId())
                .map(Paiement::getMethode)
                .orElse("Carte");
    }

    private void envoyerRecuMultiCanal(Patient patient, Facture facture) {
        String message = "🎉 Paiement confirmé ! Reçu n°" + facture.getNumero() + " envoyé à votre email.";

        // Email
        emailService.envoyerEmail(
                patient.getEmail(),
                "Votre reçu de paiement",
                message,
                facture.getUrlPdf()
        );

        // SMS
        if (patient.getNumeroTelephone() != null) {
            smsService.envoyerSms(patient.getNumeroTelephone(), message);
        }

        // WebSocket notification (optionnel)
        // notificationService.push(patient.getId(), "Reçu disponible 📄");
    }
}
