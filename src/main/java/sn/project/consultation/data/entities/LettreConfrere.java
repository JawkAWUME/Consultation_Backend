package sn.project.consultation.data.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Embeddable
public class LettreConfrere extends CorrespondanceMedicale {
    private String motifConsultation;
    private List<String> resultatsExamens;
    private String diagnostic;
    private List<String> traitementPropose;
    private List<String> recommandationsSuivi;
}