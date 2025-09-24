package sn.project.consultation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sn.project.consultation.data.entities.RendezVous;
import sn.project.consultation.data.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.coordonnees.email = :email")
    User findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.coordonnees.numeroTelephone = :tel")
    Optional<User> findByTelephone(@Param("tel") String numeroTelephone);

    @Query("SELECT u FROM User u WHERE LOWER(u.coordonnees.adresse) LIKE LOWER(CONCAT('%', :adresse, '%'))")
    List<User> searchByAdresse(@Param("adresse") String adresse);
}
