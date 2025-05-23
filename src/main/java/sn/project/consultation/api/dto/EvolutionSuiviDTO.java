package sn.project.consultation.api.dto;

import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.entities.EvolutionSuivi;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class EvolutionSuiviDTO {
    private List<String> notesEvolution; // Exemple : "Amélioration de l'état général", "Régression de la fièvre", etc.
    private Map<String, List<Double>> courbes; // Ex : {"température": [36.8, 37.1, 37.0]}
    private List<String> resumeConsultations; // Résumés des consultations de suivi

//    public static EvolutionSuiviDTO toDTO(EvolutionSuivi entity) {
//        if (entity == null) return null;
//
//        EvolutionSuiviDTO dto = new EvolutionSuiviDTO();
//        dto.setNotesEvolution(entity.getNotesEvolution());
//        dto.setCourbes(entity.getCourbes());
//        dto.setResumeConsultations(entity.getConsultationsSuivi());
//        return dto;
//    }
//
//    public static EvolutionSuivi toEntity(EvolutionSuiviDTO dto) {
//        if (dto == null) return null;
//
//        EvolutionSuivi entity = new EvolutionSuivi();
//        entity.setNotesEvolution(dto.getNotesEvolution());
//        entity.setCourbes(dto.getCourbes());
//        entity.setResumeConsultations(dto.getResumeConsultations());
//        return entity;
//    }
}
