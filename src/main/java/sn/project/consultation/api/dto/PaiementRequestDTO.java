package sn.project.consultation.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PaiementRequestDTO {
    private Long patientId;
    private Long professionnelId;
    private String methode;
}
