package sn.project.consultation.data.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sn.project.consultation.data.entities.DossierMedical;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.repositories.DossierMedicalRepository;
import sn.project.consultation.data.repositories.PatientRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(4)
public class DossierMedicalFixtures implements CommandLineRunner {
    @Autowired
    private DossierMedicalRepository dossierRepo;
    @Autowired
    private PatientRepository patientRepo;

    @Override
    public void run(String... args) {
        List<Patient> patients = patientRepo.findAll();
        List<DossierMedical> dossiers = new ArrayList<>();

        for (Patient p : patients) {
            DossierMedical dossier = new DossierMedical();
            dossier.setPatient(p);
            dossier.setResume("Dossier médical du patient " + p.getNom());
            dossier.setDocuments(new ArrayList<>());
            dossier.setHistoriques(new ArrayList<>());
            dossiers.add(dossier);
        }

        dossierRepo.saveAll(dossiers);
    }
}
