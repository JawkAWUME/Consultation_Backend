package sn.project.consultation.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Data
@Getter
@Setter
public class DossierMedicalDTO {
    private Long id;
    private Long patientId;
    private String resume;
    private List<DocumentDTO> documents;
    private List<HistoriqueConsultationDTO> historiques;
}
