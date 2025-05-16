package sn.project.consultation.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Patient extends User {

    private String adresse;
    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "patient")
    private List<RendezVous> rendezVous;
}
