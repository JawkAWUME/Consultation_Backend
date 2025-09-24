package sn.project.consultation.data.fixtures;


import sn.project.consultation.data.entities.Coordonnees;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.enums.RoleUser;
import sn.project.consultation.data.repositories.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        String[] situationsFamiliales = {"Célibataire", "Marié(e)", "Divorcé(e)", "Veuf(ve)"};
        String[] lieuxNaissance = {"Dakar", "Thiès", "Saint-Louis", "Kaolack", "Ziguinchor"};

        // Listes simples pour déterminer le sexe selon le prénom
        Set<String> prenomsMasculins = Set.of(
                "Moussa", "Ibrahima", "Abdou", "Amadou", "Lamine",
                "Cheikh", "Alioune", "Ismaila", "Seydou"
        );

        Set<String> prenomsFeminins = Set.of(
                "Aissatou", "Fatou", "Mame", "Mariam", "Ndeye",
                "Astou", "Oumou", "Aminata", "Bintou", "Salimata"
        );

        for (int i = 0; i < nomsPrenoms.length; i++) {
            String nom = nomsPrenoms[i][0];
            String prenom = nomsPrenoms[i][1];

            Patient patient = new Patient();
            patient.setNom(nom);
            patient.setPrenom(prenom);

            // Détermination du sexe selon le prénom
            if (prenomsMasculins.contains(prenom)) {
                patient.setSexe("Masculin");
            } else if (prenomsFeminins.contains(prenom)) {
                patient.setSexe("Féminin");
            } else {
                patient.setSexe("Inconnu"); // fallback si prénom non trouvé
            }

            // Coordonnées
            Coordonnees coordonnees = new Coordonnees();
            coordonnees.setEmail((prenom + "." + nom + "@santeado.com").toLowerCase());
            coordonnees.setAdresse("Rue " + (i + 1) + ", Quartier Médina");
            coordonnees.setNumeroTelephone("77" + String.format("%07d", 1000 + i));
            patient.setCoordonnees(coordonnees);

            // Sécurité & rôle
            patient.setMotDePasse("password123"); // ⚠️ à encoder avec PasswordEncoder en prod
            patient.setRole(RoleUser.PATIENT);

            // Champs spécifiques Patient
            patient.setMatricule("PAT-" + String.format("%04d", i + 1));
            patient.setLieuNaissance(lieuxNaissance[i % lieuxNaissance.length]);
            patient.setDateNaissance(LocalDate.now().minusYears(20 + (i % 15)).minusDays(i * 5L));
            patient.setSituationFamiliale(situationsFamiliales[i % situationsFamiliales.length]);

            // Géolocalisation
            patient.setLatitude(14.70 + i * 0.01);
            patient.setLongitude(-17.45 + i * 0.01);

            patients.add(patient);
        }

        patientRepository.saveAllAndFlush(patients);
    }
}

