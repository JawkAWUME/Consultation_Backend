package sn.project.consultation.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.enums.RoleUser;

@Entity
@Data
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String numeroTelephone;

    @Enumerated(EnumType.STRING)
    private RoleUser role;
}