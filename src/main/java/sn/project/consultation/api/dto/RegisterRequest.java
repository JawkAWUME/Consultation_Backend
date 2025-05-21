package sn.project.consultation.api.dto;

import lombok.Data;
import sn.project.consultation.data.enums.RoleUser;

@Data
public class RegisterRequest {
    private String email;
    private String motDePasse;
    private RoleUser role; // PATIENT ou PRO_SANTE
}
