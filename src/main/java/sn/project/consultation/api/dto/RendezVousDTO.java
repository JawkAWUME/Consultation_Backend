package sn.project.consultation.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RendezVousDTO {
    private Long id;
    private PatientDTO patient;
    private ProSanteDTO professionnel;
    private LocalDateTime dateHeure;
    private String statut;
}