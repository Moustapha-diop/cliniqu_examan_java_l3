package com.example.clinique.model;

import com.example.clinique.model.enums.GroupeSanguin;
import com.example.clinique.model.enums.Sexe;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"rendezVous", "consultations"})
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private LocalDate dateNaissance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sexe sexe;

    @Column(nullable = false)
    private String telephone;

    private String adresse;

    @Enumerated(EnumType.STRING)
    private GroupeSanguin groupeSanguin;

    @Column(columnDefinition = "TEXT")
    private String antecedentsMedicaux;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RendezVous> rendezVous;

    public String getNomComplet() {
        return prenom + " " + nom;
    }
}