package sn.project.consultation.api.dto;



import lombok.*;
import sn.project.consultation.data.entities.Facture;
import sn.project.consultation.data.entities.Paiement;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactureDTO {

    private Long id;
    private String numero;
    private LocalDateTime dateEmission;
    private String urlPdf;

    private Double montant;
    private String methode;
    private String statut;

    private PatientDTO patient;
    private ProSanteDTO professionnel;

    // ✅ Conversion Entity → DTO
    public static FactureDTO fromEntity(Facture facture) {
        if (facture == null) return null;

        FactureDTO dto = new FactureDTO();
        dto.setId(facture.getId());
        dto.setNumero(facture.getNumero());
        dto.setDateEmission(facture.getDateEmission());
        dto.setUrlPdf(facture.getUrlPdf());

        Paiement paiement = facture.getPaiement();
        if (paiement != null) {
            dto.setMontant(paiement.getMontant());
            dto.setMethode(paiement.getMethode());
            dto.setStatut(paiement.getStatut());
            dto.setPatient(PatientDTO.fromEntity(paiement.getPatient()));
            dto.setProfessionnel(ProSanteDTO.fromEntity(paiement.getProfessionnel()));
        }

        return dto;
    }

    // ✅ Conversion DTO → Entity (facultative ici)
    public static Facture toEntity(FactureDTO dto) {
        if (dto == null) return null;

        Facture facture = new Facture();
        facture.setId(dto.getId());
        facture.setNumero(dto.getNumero());
        facture.setDateEmission(dto.getDateEmission());
        facture.setUrlPdf(dto.getUrlPdf());

        return facture;
    }
}
