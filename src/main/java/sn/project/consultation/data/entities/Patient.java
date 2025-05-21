package sn.project.consultation.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@Setter
public class Patient extends User {

    private String matricule;
    private Double latitude;
    private String lieuNaissance;
    private LocalDateTime dateNaissance;
    private Double longitude;
    private String situationFamiliale;

    @OneToMany(mappedBy = "patient")
    private List<RendezVous> rendezVous;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
