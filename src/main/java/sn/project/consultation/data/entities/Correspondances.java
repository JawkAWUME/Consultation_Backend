package sn.project.consultation.data.entities;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Correspondances {
    @Embedded
    private CompteRenduHospitalisation compteRenduHospitalisation;

    @Embedded
    private CompteRenduOperatoire compteRenduOperatoire;

    @Embedded
    private LettreConfrere lettreConfrere;
}
