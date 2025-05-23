package sn.project.consultation.data.entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class DossierMedical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resume;

    @ManyToOne
    private Patient patient;

    private String couvertureSociale;
    private String personneUrgence;
    private String telPersonneUrgence;

    @OneToMany(mappedBy = "dossier", cascade = CascadeType.ALL)
    private List<DocumentMedical> documents;

    @Embedded
    private Antecedents antecedents;

    @Embedded
    private ExamenClinique examenClinique;

    @Embedded
    private ExamensComplementaires examensComplementaires;

    @Embedded
    private DiagnosticMedical diagnosticMedical;

    @Embedded
    private TraitementPrescription traitements;

    @OneToOne(mappedBy = "dossierMedical", cascade = CascadeType.ALL)
    private EvolutionSuivi evolutionSuivi;

    @Embedded
    private Correspondances correspondances;

    @OneToMany(mappedBy = "dossier", cascade = CascadeType.ALL)
    private List<FichierMedical> documentsAnnexes;

    @OneToMany(mappedBy = "dossier", cascade = CascadeType.ALL)
    private List<HistoriqueConsultation> historiques;

}
