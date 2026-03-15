package com.example.clinique.model;

import com.example.clinique.model.enums.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String motDePasseHash;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean actif = true;
}