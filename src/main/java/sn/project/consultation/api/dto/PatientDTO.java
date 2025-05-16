package sn.project.consultation.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.enums.RoleUser;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Long id;
    private String nom;
    private String adresse;
    private String email;
    private RoleUser role;

    public static PatientDTO fromEntity(Patient patient) {
        if (patient == null) {
            return null;
        }

        return new PatientDTO(
                patient.getId(),
                patient.getNom()+" "+patient.getPrenom(),  // Assure-toi que la classe User (superclasse) a bien la méthode getNom()
                patient.getAdresse(),
                patient.getEmail(),
                patient.getRole()
                // distanceKm sera probablement calculée ailleurs
        );
    }

    public static Patient toEntity(PatientDTO dto) {
        if (dto == null) {
            return null;
        }

        Patient patient = new Patient();
        patient.setId(dto.getId());

        // Séparation du nom et prénom si possible
        String[] parts = dto.getNom() != null ? dto.getNom().split(" ", 2) : new String[]{"", ""};
        patient.setNom(parts[0]);
        patient.setPrenom(parts.length > 1 ? parts[1] : "");

        patient.setAdresse(dto.getAdresse());
        patient.setEmail(dto.getEmail());
        patient.setRole(dto.getRole());

        return patient;
    }
}