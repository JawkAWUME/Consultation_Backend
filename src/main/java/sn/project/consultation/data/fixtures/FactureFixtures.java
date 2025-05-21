package sn.project.consultation.data.fixtures;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
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
public class FactureFixtures implements CommandLineRunner {

    private final PaiementRepository paiementRepo;
    private final FactureRepository factureRepo;

    @Override
    public void run(String... args) {
        if (factureRepo.count() > 0) return;

        Faker faker = new Faker(new Locale("fr"));
        List<Paiement> paiementsSansFacture = paiementRepo.findAll()
                .stream()
                .filter(p -> p.getFacture() == null && "SUCCES".equals(p.getStatut()))
                .collect(Collectors.toList());

        for (Paiement paiement : paiementsSansFacture) {
            Facture facture = new Facture();
            facture.setDateEmission(paiement.getDatePaiement().plusMinutes(10));
            facture.setNumero("FAC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            facture.setUrlPdf("https://example.com/factures/" + facture.getNumero() + ".pdf");

            facture.setPaiement(paiement);
            paiement.setFacture(facture);

            // ✅ grâce au cascade = ALL sur Paiement -> Facture, on peut simplement sauvegarder Paiement
            paiementRepo.save(paiement);
        }
    }
}
