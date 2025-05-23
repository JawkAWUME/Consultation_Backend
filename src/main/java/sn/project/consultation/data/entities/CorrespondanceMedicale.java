package sn.project.consultation.data.entities;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public abstract class CorrespondanceMedicale {
    private LocalDate dateRedaction;
    @ManyToOne
    @JoinColumn(name = "auteur_id")
    private ProSante auteur; // médecin, service, etc.

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private ProSante destinataire; // médecin traitant, autre spécialiste, etc.

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient; // identifiant du patient concerné
}
