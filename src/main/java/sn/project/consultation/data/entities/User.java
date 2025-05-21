package sn.project.consultation.data.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import sn.project.consultation.data.enums.RoleUser;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String sexe;

    private String motDePasse;

    @Embedded
    private Coordonnees coordonnees;

    @Enumerated(EnumType.STRING)
    private RoleUser role;


    public boolean isAccountNonExpired() { return true; }


    public boolean isAccountNonLocked() { return true; }


    public boolean isCredentialsNonExpired() { return true; }


    public boolean isEnabled() { return true; }
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
//    }
}