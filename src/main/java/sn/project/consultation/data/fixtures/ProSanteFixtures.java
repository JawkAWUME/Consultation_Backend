package sn.project.consultation.data.fixtures;

import sn.project.consultation.data.entities.Coordonnees;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.enums.RoleUser;
import sn.project.consultation.data.repositories.ProSanteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Order(2)
public class ProSanteFixtures implements CommandLineRunner {

    @Autowired
    private ProSanteRepository proSanteRepository;

    @Override
    public void run(String... args) {
        List<ProSante> pros = new ArrayList<>();
        Random random = new Random();

        String[][] nomsPrenoms = {
                {"Sarr", "Mamadou"}, {"Faye", "Khady"}, {"Mbaye", "Elhadj"}, {"Gueye", "Yacine"},
                {"Seck", "Papa"}, {"Lo", "Aminata"}, {"Ndoye", "Alioune"}, {"Niane", "Awa"},
                {"Balde", "Tidiane"}, {"Kebe", "Moussa"}, {"Sylla", "Nene"}, {"Dia", "Adama"},
                {"Dieng", "Malick"}, {"Niang", "Fatou"}, {"Ka", "Serigne"}, {"Sakho", "Mame"},
                {"Ba", "Oumar"}, {"Barry", "Soukeyna"}, {"Diouf", "Abdoulaye"}, {"Fall", "Bineta"}
        };

        String[] specialites = {
                "Généraliste", "Pédiatre", "Cardiologue", "Gynécologue", "Dentiste",
                "Dermatologue", "Ophtalmologue", "Orthopédiste", "ORL", "Neurologue"
        };

        // Quartiers de Dakar avec leurs coordonnées approximatives (lat, lng)
        double[][] quartiers = {
                {14.73, -17.44}, // Almadies
                {14.72, -17.47}, // Ouakam
                {14.68, -17.46}, // Fann
                {14.69, -17.45}, // Plateau
                {14.71, -17.44}, // Mermoz
                {14.70, -17.46}  // Médina
        };

        for (int i = 0; i < nomsPrenoms.length; i++) {
            String nom = nomsPrenoms[i][0];
            String prenom = nomsPrenoms[i][1];
            String specialite = specialites[i % specialites.length];

            ProSante pro = new ProSante();
            pro.setNom(nom);
            pro.setPrenom(prenom);
            Coordonnees coordonnees = new Coordonnees();
            coordonnees.setEmail((prenom + "." + nom + "@santeado.com").toLowerCase());
            pro.setMotDePasse("securepass456"); // à encoder si nécessaire
            coordonnees.setNumeroTelephone("78" + String.format("%07d", 2000 + i));
            pro.setCoordonnees(coordonnees);
            pro.setRole(RoleUser.PRO_SANTE);
            pro.setSpecialite(specialite);
            pro.setDescription("Médecin spécialisé en " + specialite.toLowerCase() + " avec plus de " + (3 + i % 5) + " ans d'expérience.");
            pro.setTarif(10000.0 + (i * 1000));

            // Choix du quartier selon l'index modulo la taille du tableau quartiers
            int quartierIndex = i % quartiers.length;
            double baseLat = quartiers[quartierIndex][0];
            double baseLng = quartiers[quartierIndex][1];

            // Décalage aléatoire léger pour éviter que tous soient exactement au même point
            double offsetLat = (random.nextDouble() - 0.5) * 0.01; // ±0.005
            double offsetLng = (random.nextDouble() - 0.5) * 0.01; // ±0.005

            pro.setLatitude(baseLat + offsetLat);
            pro.setLongitude(baseLng + offsetLng);

            pros.add(pro);
        }

        proSanteRepository.saveAllAndFlush(pros);
    }
}
