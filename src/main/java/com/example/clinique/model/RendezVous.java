package com.example.clinique.model;

import com.example.clinique.model.enums.StatutRendezVous;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medecin_id", nullable = false)
    private Medecin medecin;

    @Column(nullable = false)
    private LocalDateTime dateHeure;

    @Column(columnDefinition = "TEXT")
    private String motif;

    @Enumerated(EnumType.STRING)
    private StatutRendezVous statut = StatutRendezVous.PROGRAMME;


}