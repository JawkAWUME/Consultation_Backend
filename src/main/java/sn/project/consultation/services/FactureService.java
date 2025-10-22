package sn.project.consultation.services;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.project.consultation.api.dto.FactureDTO;
import sn.project.consultation.data.entities.Facture;
import sn.project.consultation.data.entities.Paiement;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.repositories.FactureRepository;
import sn.project.consultation.data.repositories.PatientRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FactureService {

    @Autowired private FactureRepository factureRepo;
    @Autowired private PatientRepository patientRepo;
    @Autowired private PDFGeneratorService pdfGenerator;
    @Autowired private EmailService emailService;
    @Autowired private CloudStorageService cloudStorage;

    @Transactional
    public Facture genererEtEnvoyerFacture(Paiement paiement) {
        try {
            String numero = genererNumeroFacture(paiement);
            byte[] pdf = pdfGenerator.genererFacturePDF(paiement, numero);
            String urlStockage = cloudStorage.upload(pdf, "factures/" + numero + ".pdf");

            Facture facture = new Facture();
            facture.setNumero(numero);
            facture.setDateEmission(paiement.getDatePaiement());
            facture.setPaiement(paiement);
            facture.setUrlPdf(urlStockage);

            facture = factureRepo.save(facture);

            emailService.envoyerEmail(
                    paiement.getPatient().getCoordonnees().getEmail(),
                    "📄 Votre facture #" + numero,
                    "Merci pour votre paiement. Votre facture est jointe ou consultable ici : " + urlStockage,
                    pdf,
                    "facture-" + numero + ".pdf"
            );

            log.info("Facture {} générée et envoyée à {}", numero, paiement.getPatient().getCoordonnees().getEmail());
            return facture;

        } catch (Exception e) {
            log.error("Erreur lors de la génération/envoi de facture : {}", e.getMessage(), e);
            throw new RuntimeException("Impossible de générer ou envoyer la facture", e);
        }
    }

    public List<FactureDTO> getFacturesByPatient(Long patientId) {
        Patient patient = patientRepo.findById(patientId).orElseThrow();
        List<Facture> factures = factureRepo.findByPaiement_PatientOrderByDateEmissionDesc(patient);
        return factures.stream()
                .map(FactureDTO::fromEntity)
                .collect(Collectors.toList());
    }
    private String genererNumeroFacture(Paiement paiement) {
        String date = paiement.getDatePaiement().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm"));
        return "FAC-" + date + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}

