package sn.project.consultation.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProSanteDTO {
    private Long id;
    private String nom;
    private String specialite;
    private Double tarif;
    private Double latitude;
    private Double longitude;
    private Double distanceKm;
}
