package sn.project.consultation.data.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Embeddable
@Getter
@Setter
public class Antecedents {

    // Antécédents personnels
    @ElementCollection
    private List<String> antecedentsMedicaux;

    @ElementCollection// Exemple : Diabète, hypertension...
    private List<String> antecedentsChirurgicaux;  // Exemple : Appendicectomie...

    @ElementCollection
    private List<String> antecedentsObstetricaux;  // Surtout pour les patientes

    @ElementCollection
    private List<String> antecedentsPsychologiques;
    // Antécédents familiaux

    @ElementCollection
    private List<String> maladiesFamiliales;       // Exemple : Antécédents familiaux de cancer, diabète...

    // Allergies
    @ElementCollection
    private List<String> allergies;                // Médicamenteuses, alimentaires, etc.
}
