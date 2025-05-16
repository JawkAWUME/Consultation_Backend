package sn.project.consultation.api.dto;

import lombok.Data;
import java.time.LocalDate;
import sn.project.consultation.data.entities.HistoriqueConsultation;


@Data
public class HistoriqueConsultationDTO {
    private Long id;
    private LocalDate date;
    private String notes;
    private String traitement;

    public static HistoriqueConsultationDTO fromEntity(HistoriqueConsultation entity) {
        if (entity == null) {
            return null;
        }

        HistoriqueConsultationDTO dto = new HistoriqueConsultationDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setNotes(entity.getNotes());
        dto.setTraitement(entity.getTraitement());

        return dto;
    }

    public static HistoriqueConsultation toEntity(HistoriqueConsultationDTO dto) {
        if (dto == null) {
            return null;
        }

        HistoriqueConsultation entity = new HistoriqueConsultation();
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setNotes(dto.getNotes());
        entity.setTraitement(dto.getTraitement());

        return entity;
    }
}
