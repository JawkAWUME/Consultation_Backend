package sn.project.consultation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.project.consultation.data.entities.DocumentMedical;
import sn.project.consultation.data.entities.DossierMedical;


public interface DocumentMedicalRepository extends JpaRepository<DocumentMedical, Long> {

}
