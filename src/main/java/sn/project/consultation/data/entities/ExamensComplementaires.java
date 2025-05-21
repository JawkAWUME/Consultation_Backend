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
    private Map<String, String> analysesSanguines;     // Exemple: {"Glycémie": "1.2 g/L", "CRP": "15 mg/L"}

    @ElementCollection
    private Map<String, String> analysesUrines;        // Exemple: {"Protéinurie": "Négatif", "Leucocytes": "Présents"}

    // 🖼️ Examens d’imagerie
    @ElementCollection
    private List<String> radiographies;                // Exemple: "Radio thorax: opacité pulmonaire", "Radio bassin: normal"

    @ElementCollection
    private List<String> echographies;                 // Exemple: "Écho abdominale: foie homogène"

    @ElementCollection
    private List<String> scanners;                     // Exemple: "Scanner cérébral: pas d'anomalie"

    @ElementCollection
    private List<String> irm;                          // Exemple: "IRM genou: rupture LCA"

    // ⚙️ Tests spécialisés
    @ElementCollection
    private Map<String, String> testsSpeciaux;         // Exemple: {"ECG": "sinusal normal", "Spirométrie": "obstruction légère"}

}
