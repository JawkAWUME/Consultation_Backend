package sn.project.consultation.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Schema(description = "Représente les examens complémentaires réalisés pour un patient")
public class ExamensComplementairesDTO {

    // 🔬 Analyses biologiques
    @Schema(description = "Résultats des analyses sanguines", example = "{\"Glycémie\": \"1.2 g/L\", \"CRP\": \"15 mg/L\"}")
    private Map<String, String> analysesSanguines;

    @Schema(description = "Résultats des analyses urinaires", example = "{\"Protéinurie\": \"Négatif\", \"Leucocytes\": \"Présents\"}")
    private Map<String, String> analysesUrines;

    // 🖼️ Examens d’imagerie
    @Schema(description = "Résultats de radiographies", example = "[\"Radio thorax: opacité pulmonaire\", \"Radio bassin: normal\"]")
    private List<String> radiographies;

    @Schema(description = "Résultats des échographies", example = "[\"Écho abdominale: foie homogène\"]")
    private List<String> echographies;

    @Schema(description = "Résultats des scanners", example = "[\"Scanner cérébral: pas d'anomalie\"]")
    private List<String> scanners;

    @Schema(description = "Résultats des IRM", example = "[\"IRM genou: rupture LCA\"]")
    private List<String> irm;

    // ⚙️ Tests spécialisés
    @Schema(description = "Résultats des tests spécialisés", example = "{\"ECG\": \"rythme sinusal\", \"Spirométrie\": \"obstruction légère\"}")
    private Map<String, String> testsSpeciaux;
}
