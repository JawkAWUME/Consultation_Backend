package sn.project.consultation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.project.consultation.data.entities.ProSante;

import java.util.List;

public interface ProSanteRepository extends JpaRepository<ProSante, Long> {
    List<ProSante> findBySpecialiteContainingIgnoreCase(String specialite);
    List<ProSante> findByTarifLessThanEqual(Double tarif);
}