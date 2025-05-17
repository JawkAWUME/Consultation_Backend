package sn.project.consultation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.project.consultation.data.entities.RendezVous;
import sn.project.consultation.data.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
