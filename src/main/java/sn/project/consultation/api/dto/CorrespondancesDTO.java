package sn.project.consultation.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CorrespondancesDTO {
    private String compteRenduOperatoire;
    private String compteRenduHospitalisation;
    private String lettreMedecinTraitant;
}
