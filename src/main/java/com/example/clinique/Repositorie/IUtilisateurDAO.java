package com.example.clinique.Repositorie;

import com.example.clinique.model.Utilisateur;

import java.util.Optional;

public interface IUtilisateurDAO extends GenericDAO<Utilisateur> {
    Optional<Utilisateur> findByUsername(String username);
}
