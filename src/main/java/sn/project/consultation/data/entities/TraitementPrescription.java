package sn.project.consultation.data.entities;


import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Embeddable
@Getter
@Setter
public class TraitementPrescription {

    // Médicaments prescrits
    @ElementCollection
    private List<MedicamentPrescrit> medicaments;

    // Soins infirmiers ou paramédicaux
    @ElementCollection
    private List<SoinsParamedicaux> soins;

    // Interventions ou procédures chirurgicales prévues
    @ElementCollection
    private List<InterventionChirurgicale> interventions;
}
