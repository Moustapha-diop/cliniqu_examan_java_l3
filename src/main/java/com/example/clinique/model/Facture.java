package com.example.clinique.model;

import com.example.clinique.model.enums.ModePaiement;
import com.example.clinique.model.enums.StatutPaiement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    @Column(nullable = false)
    private LocalDateTime dateFacture;

    @Column(nullable = false)
    private double montantTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModePaiement modePaiement;

    @Enumerated(EnumType.STRING)
    private StatutPaiement statut = StatutPaiement.NON_PAYE;

    public String getNumeroFacture() {
        return String.format("FAC-%04d", id);
    }
}