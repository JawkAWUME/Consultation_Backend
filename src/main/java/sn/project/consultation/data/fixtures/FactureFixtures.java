package sn.project.consultation.data.fixtures;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sn.project.consultation.data.entities.Facture;
import sn.project.consultation.data.entities.Paiement;
import sn.project.consultation.data.repositories.FactureRepository;
import sn.project.consultation.data.repositories.PaiementRepository;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Order(7)
public class FactureFixtures implements CommandLineRunner {

    private final PaiementRepository paiementRepo;
    private final FactureRepository factureRepo;

    @Override
    public void run(String... args) {
        if (factureRepo.count() > 0) return;

        Faker faker = new Faker(new Locale("fr"));
        List<Paiement> paiements = paiementRepo.findAll();

        for (Paiement paiement : paiements) {
            // Si le paiement a déjà une facture (au cas où fixtures rejouées)
            if (paiement.getFacture() != null) continue;

            Facture facture = new Facture();
            facture.setDateEmission(
                    paiement.getDatePaiement() != null
                            ? paiement.getDatePaiement().plusMinutes(10)
                            : faker.date().past(30, java.util.concurrent.TimeUnit.DAYS)
                            .toInstant().atZone(java.time.ZoneId.systemDefault())
                            .toLocalDateTime()
            );

            // Numéro unique
            facture.setNumero("FAC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

            // URL PDF simulée (on peut imaginer un endpoint de génération)
            facture.setUrlPdf("https://example.com/factures/" + facture.getNumero() + ".pdf");

            // Lien bidirectionnel
            facture.setPaiement(paiement);
            paiement.setFacture(facture);

            // ✅ Sauvegarde automatique grâce au cascade entre Paiement et Facture
            paiementRepo.save(paiement);
        }
    }
}
