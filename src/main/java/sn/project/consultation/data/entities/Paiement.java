package sn.project.consultation.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double montant;
    private LocalDateTime datePaiement;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private ProSante professionnel;

    @OneToOne(mappedBy = "paiement", cascade = CascadeType.ALL)
    private Facture facture;

    private String methode; // "Carte", "MobileMoney", "Virement"
    private String statut;  // "SUCCES", "ECHEC", "EN_ATTENTE"
}
