package sn.project.consultation.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProSante extends User {

    private String specialite;
    private String description;
    private Double tarif;
    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "prosante")
    private List<RendezVous> rendezVous;

    @OneToMany(mappedBy = "prosante")
    private List<Evaluation> evaluations;
}
