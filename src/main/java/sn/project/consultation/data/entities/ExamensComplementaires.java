package sn.project.consultation.data.entities;


import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
@Embeddable
@Getter
@Setter
public class ExamensComplementaires {

    // 🔬 Analyses biologiques
    @ElementCollection
    private List<AnalyseBiologique> analysesSanguines;

    @ElementCollection
    private List<AnalyseBiologique> analysesUrines;

    // 🖼️ Examens d’imagerie
    @ElementCollection
    private List<String> radiographies;

    @ElementCollection
    private List<String> echographies;

    @ElementCollection
    private List<String> scanners;

    @ElementCollection
    private List<String> irm;

    // ⚙️ Tests spécialisés
    @ElementCollection
    private List<TestSpecial> testsSpeciaux;
}
