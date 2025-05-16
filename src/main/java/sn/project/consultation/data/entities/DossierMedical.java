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

    @OneToMany(mappedBy = "dossier", cascade = CascadeType.ALL)
    private List<DocumentMedical> documents;

    @OneToMany(mappedBy = "dossier", cascade = CascadeType.ALL)
    private List<HistoriqueConsultation> historiques;
}
