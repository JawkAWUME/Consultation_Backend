package sn.project.consultation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.project.consultation.data.entities.Patient;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
  Patient findByNomIgnoreCaseAndPrenomIgnoreCase(String nom, String prenom);
}