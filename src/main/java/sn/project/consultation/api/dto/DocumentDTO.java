package sn.project.consultation.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DocumentDTO {
    private String nom;
    private String urlStockage;
}
