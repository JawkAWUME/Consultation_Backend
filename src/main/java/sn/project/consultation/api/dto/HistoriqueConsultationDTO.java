package sn.project.consultation.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import sn.project.consultation.data.entities.DossierMedical;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class HistoriqueConsultationDTO {
    private Long id;
    private LocalDate date;
    private String notes;
    private String traitement;
    private DossierMedicalDTO dossier;
}
