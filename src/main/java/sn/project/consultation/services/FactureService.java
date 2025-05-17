package sn.project.consultation.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.project.consultation.data.entities.Facture;
import sn.project.consultation.data.entities.Paiement;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.repositories.FactureRepository;
import sn.project.consultation.data.repositories.PatientRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class FactureService {

    @Autowired
    private FactureRepository factureRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private PDFGeneratorService pdfGenerator;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CloudStorageService cloudStorage;

    /**
     * Génère une facture PDF, l’enregistre et l’envoie au patient.
     */
    public Facture genererEtEnvoyerFacture(Paiement paiement) {
        // 🔢 Numérotation intelligente
        String numero = genererNumeroFacture(paiement);

        // 📄 Génération PDF
        byte[] pdf = pdfGenerator.genererFacturePDF(paiement, numero);

        // ☁️ Stockage cloud/local
        String urlStockage = cloudStorage.upload(pdf, "factures/" + numero + ".pdf");

        // 🧾 Création de l’objet facture
        Facture facture = new Facture();
        facture.setNumero(numero);
        facture.setDateEmission(paiement.getDatePaiement());
        facture.setPaiement(paiement);
        facture.setUrlPdf(urlStockage);

        facture = factureRepo.save(facture);

        // 📧 Envoi automatique au patient
        emailService.envoyerEmail(
                paiement.getPatient().getEmail(),
                "📄 Votre facture #" + numero,
                "Merci pour votre paiement. Votre facture est jointe en pièce jointe ou disponible ici : " + urlStockage,
                pdf,
                "facture-" + numero + ".pdf"
        );

        return facture;
    }

    public List<Facture> getFacturesByPatient(Long patientId) {
        Patient patient=patientRepo.findById(patientId).orElseThrow();
        return factureRepo.findByPaiement_PatientOrderByDateEmissionDesc(patient);
    }
    /**
     * Numérotation basée sur UUID + date
     */
    private String genererNumeroFacture(Paiement paiement) {
        String date = paiement.getDatePaiement().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm"));
        return "FAC-" + date + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
