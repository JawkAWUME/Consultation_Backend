package sn.project.consultation.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


import lombok.Data;
import sn.project.consultation.data.entities.DossierMedical;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class DossierMedicalDTO {
    private Long id;
    private PatientDTO patient;
    private String resume;
    private List<DocumentDTO> documents;
    private List<HistoriqueConsultationDTO> historiques;

    public static DossierMedicalDTO fromEntity(DossierMedical dossier) {
        if (dossier == null) {
            return null;
        }

        DossierMedicalDTO dto = new DossierMedicalDTO();
        dto.setId(dossier.getId());
        dto.setResume(dossier.getResume());
        dto.setPatient(PatientDTO.fromEntity(dossier.getPatient()));

        if (dossier.getDocuments() != null) {
            dto.setDocuments(dossier.getDocuments()
                    .stream()
                    .map(DocumentDTO::fromEntity)
                    .collect(Collectors.toList()));
        }

        if (dossier.getHistoriques() != null) {
            dto.setHistoriques(dossier.getHistoriques()
                    .stream()
                    .map(HistoriqueConsultationDTO::fromEntity)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static DossierMedical toEntity(DossierMedicalDTO dto) {
        if (dto == null) {
            return null;
        }

        DossierMedical dossier = new DossierMedical();
        dossier.setId(dto.getId());
        dossier.setResume(dto.getResume());
        dossier.setPatient(PatientDTO.toEntity(dto.getPatient()));

        if (dto.getDocuments() != null) {
            dossier.setDocuments(dto.getDocuments()
                    .stream()
                    .map(DocumentDTO::toEntity)
                    .collect(Collectors.toList()));
        }

        if (dto.getHistoriques() != null) {
            dossier.setHistoriques(dto.getHistoriques()
                    .stream()
                    .map(HistoriqueConsultationDTO::toEntity)
                    .collect(Collectors.toList()));
        }

        return dossier;
    }
}
