package sn.project.consultation.api.dto;


import lombok.Data;

import java.util.List;

@Data
public class DiagnosticMedicalDTO {

    private String diagnosticPrincipal;
    private String codePrincipal;
    private String systemeCodification;
    private List<String> diagnosticsSecondaires;
}
