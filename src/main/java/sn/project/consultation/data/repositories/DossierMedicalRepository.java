package sn.project.consultation.data.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import sn.project.consultation.data.entities.DossierMedical;

import java.util.Optional;

public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Long> {
           Optional<DossierMedical> findByPatientId(Long patientId);
}