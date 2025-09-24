package sn.project.consultation.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.entities.AnalyseBiologique;
import sn.project.consultation.data.entities.ElementBilanPhysique;
import sn.project.consultation.data.entities.ExamensComplementaires;
import sn.project.consultation.data.entities.TestSpecial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Getter
@Setter
@Schema(description = "Représente les examens complémentaires réalisés pour un patient")
public class ExamensComplementairesDTO {

    // 🔬 Analyses biologiques
    @Schema(description = "Résultats des analyses sanguines", example = "{\"Glycémie\": \"1.2 g/L\", \"CRP\": \"15 mg/L\"}")
    private List<Map<String, String>> analysesSanguines;

    @Schema(description = "Résultats des analyses urinaires", example = "{\"Protéinurie\": \"Négatif\", \"Leucocytes\": \"Présents\"}")
    private List<Map<String, String>> analysesUrines;

    // 🖼️ Examens d’imagerie
    private List<String> radiographies;
    private List<String> echographies;
    private List<String> scanners;
    private List<String> irm;

    // ⚙️ Tests spécialisés
    private Map<String, String> testsSpeciaux;

    public static ExamensComplementairesDTO toDTO(ExamensComplementaires entity) {
        if (entity == null) return null;

        ExamensComplementairesDTO dto = new ExamensComplementairesDTO();

        // Analyses sanguines
        if (entity.getAnalysesSanguines() != null) {
            List<Map<String, String>> analyses = new ArrayList<>();
            for (AnalyseBiologique analyse : entity.getAnalysesSanguines()) {
                Map<String, String> map = new HashMap<>();
                map.put(analyse.getNom(), analyse.getValeur());
                analyses.add(map);
            }
            dto.setAnalysesSanguines(analyses);
        }

        // Analyses urines
        if (entity.getAnalysesUrines() != null) {
            List<Map<String, String>> analyses = new ArrayList<>();
            for (AnalyseBiologique analyse : entity.getAnalysesUrines()) {
                Map<String, String> map = new HashMap<>();
                map.put(analyse.getNom(), analyse.getValeur());
                analyses.add(map);
            }
            dto.setAnalysesUrines(analyses);
        }

        dto.setRadiographies(entity.getRadiographies());
        dto.setEchographies(entity.getEchographies());
        dto.setScanners(entity.getScanners());
        dto.setIrm(entity.getIrm());
        if (entity.getTestsSpeciaux() != null) {
            Map<String, String> testsMap = new HashMap<>();
            for (TestSpecial test : entity.getTestsSpeciaux()) {
                testsMap.put(test.getNom(), test.getResultat());
            }
            dto.setTestsSpeciaux(testsMap);
        }

        return dto;
    }

    public static ExamensComplementaires toEntity(ExamensComplementairesDTO dto) {
        if (dto == null) return null;

        ExamensComplementaires entity = new ExamensComplementaires();

        // Analyses sanguines
        if (dto.getAnalysesSanguines() != null) {
            List<AnalyseBiologique> analyses = new ArrayList<>();
            for (Map<String, String> map : dto.getAnalysesSanguines()) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    analyses.add(new AnalyseBiologique(entry.getKey(), entry.getValue()));
                }
            }
            entity.setAnalysesSanguines(analyses);
        }

        // Analyses urines
        if (dto.getAnalysesUrines() != null) {
            List<AnalyseBiologique> analyses = new ArrayList<>();
            for (Map<String, String> map : dto.getAnalysesUrines()) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    analyses.add(new AnalyseBiologique(entry.getKey(), entry.getValue()));
                }
            }
            entity.setAnalysesUrines(analyses);
        }

        entity.setRadiographies(dto.getRadiographies());
        entity.setEchographies(dto.getEchographies());
        entity.setScanners(dto.getScanners());
        entity.setIrm(dto.getIrm());
        if (dto.getTestsSpeciaux() != null) {
            List<TestSpecial> tests = new ArrayList<>();
            for (Map.Entry<String, String> entry : dto.getTestsSpeciaux().entrySet()) {
                tests.add(new TestSpecial(entry.getKey(), entry.getValue()));
            }
            entity.setTestsSpeciaux(tests);
        }

        return entity;
    }
}

