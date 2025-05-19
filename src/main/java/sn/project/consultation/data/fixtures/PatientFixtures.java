package sn.project.consultation.data.fixtures;


import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.enums.RoleUser;
import sn.project.consultation.data.repositories.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(1)
public class PatientFixtures implements CommandLineRunner {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public void run(String... args) {
        List<Patient> patients = new ArrayList<>();

        String[][] nomsPrenoms = {
                {"Cissé", "Moussa"}, {"Ba", "Aissatou"}, {"Diallo", "Ibrahima"}, {"Camara", "Fatou"},
                {"Diop", "Abdou"}, {"Kane", "Mame"}, {"Touré", "Moussa"}, {"Sow", "Mariam"},
                {"Barry", "Amadou"}, {"Fall", "Ndeye"}, {"Keita", "Lamine"}, {"Sy", "Astou"},
                {"Konaté", "Cheikh"}, {"Coulibaly", "Oumou"}, {"Ndiaye", "Alioune"}, {"Cissé", "Aminata"},
                {"Kouyaté", "Ismaila"}, {"Diakité", "Bintou"}, {"Doumbia", "Seydou"}, {"Traoré", "Salimata"}
        };

        for (int i = 0; i < nomsPrenoms.length; i++) {
            String nom = nomsPrenoms[i][0];
            String prenom = nomsPrenoms[i][1];
            Patient patient = new Patient();
            patient.setNom(nom);
            patient.setPrenom(prenom);
            patient.setEmail((prenom + "." + nom + "@santeado.com").toLowerCase());
            patient.setMotDePasse("password123"); // à encoder si nécessaire
            patient.setNumeroTelephone("77" + String.format("%07d", 1000 + i));
            patient.setRole(RoleUser.PATIENT);
            patient.setAdresse("Rue " + (i + 1) + ", Quartier Médina");
            patient.setLatitude(14.70 + i * 0.01);
            patient.setLongitude(-17.45 + i * 0.01);

            patients.add(patient);
        }

        patientRepository.saveAllAndFlush(patients);
    }
}

