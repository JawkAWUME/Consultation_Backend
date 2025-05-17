package sn.project.consultation.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RendezVousRequestDTO {
    private Long id;
    private String patient;
    private String professionnel;
    private LocalDateTime dateHeure;
    private String statut;
}
