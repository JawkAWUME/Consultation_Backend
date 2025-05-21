package sn.project.consultation.data.fixtures;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.project.consultation.data.entities.Paiement;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.repositories.PaiementRepository;
import sn.project.consultation.data.repositories.PatientRepository;
import sn.project.consultation.data.repositories.ProSanteRepository;

import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class PaiementFixtures implements CommandLineRunner {

    private final PaiementRepository paiementRepo;
    private final PatientRepository patientRepo;
    private final ProSanteRepository proRepo;

    @Override
    public void run(String... args) {
        if (paiementRepo.count() > 0) return;

        Faker faker = new Faker(new Locale("fr"));

        List<Patient> patients = patientRepo.findAll();
        List<ProSante> pros = proRepo.findAll();

        for (int i = 0; i < 25; i++) {
            Patient patient = faker.options().nextElement(patients);
            ProSante pro = faker.options().nextElement(pros);

            Paiement paiement = new Paiement();
            paiement.setMontant(pro.getTarif());
            paiement.setPatient(patient);
            paiement.setProfessionnel(pro);
            paiement.setDatePaiement(faker.date().past(60, TimeUnit.DAYS)
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            paiement.setMethode(faker.options().option("Carte", "MobileMoney", "Virement"));
            paiement.setStatut(faker.options().option("SUCCES", "ECHEC", "EN_ATTENTE"));

            paiementRepo.save(paiement);
        }
    }
}
