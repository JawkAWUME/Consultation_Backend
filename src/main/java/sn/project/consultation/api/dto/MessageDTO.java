package sn.project.consultation.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class MessageDTO {
    private Long teleconsultationId;
    private Long expediteurId;
    private String contenu;
    private String type; // "TEXTE", "DOCUMENT"
    private String documentUrl; // si type == DOCUMENT
    private LocalDateTime dateEnvoi;

}
