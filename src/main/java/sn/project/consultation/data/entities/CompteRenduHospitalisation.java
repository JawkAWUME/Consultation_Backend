package sn.project.consultation.data.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CompteRenduHospitalisation extends CorrespondanceMedicale {
    private LocalDate dateAdmission;
    private LocalDate dateSortie;
    private List<String> diagnosticAdmission;
    private List<String> diagnosticSortie;
    private List<String> examensEffectues;
    private List<String> traitements;
    private List<String> evolution;
    private List<String> recommandationsSortie;
}