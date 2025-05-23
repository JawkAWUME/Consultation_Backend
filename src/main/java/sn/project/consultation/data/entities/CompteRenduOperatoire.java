package sn.project.consultation.data.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Embeddable
public class CompteRenduOperatoire extends CorrespondanceMedicale {
    private String nomIntervention;
    private String indicationOperatoire;
    private String descriptionActe;
    private List<String> complications; // Peut être null si aucune
    private String conclusion;
}