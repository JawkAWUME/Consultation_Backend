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
        String numero = genererNumeroFacture(paiement);
        byte[] pdf = pdfGenerator.genererFacturePDF(paiement, numero);
        String urlStockage = cloudStorage.upload(pdf, "factures/" + numero + ".pdf");

        Facture facture = new Facture();
        facture.setNumero(numero);
        facture.setDateEmission(paiement.getDatePaiement());
        facture.setPaiement(paiement);
        facture.setUrlPdf(urlStockage);

        facture = factureRepo.save(facture);

        // Envoi par email avec pièce jointe (ByteArrayResource)
        emailService.envoyerEmail(
                "jawkstwitter@gmail.com",
                "📄 Votre facture #" + numero,
                "Merci pour votre paiement. Votre facture est jointe ou consultable ici : " + urlStockage,
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
