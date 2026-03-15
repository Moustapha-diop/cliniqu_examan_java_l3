package com.example.clinique.service;

import com.example.clinique.Repositorie.IUtilisateurDAO;
import com.example.clinique.model.Utilisateur;
import com.example.clinique.model.enums.Role;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UtilisateurService {

    private final IUtilisateurDAO dao;

    public UtilisateurService(IUtilisateurDAO dao) {
        this.dao = dao;
    }

    public List<Utilisateur> getAll() {
        return dao.getAll();
    }

    public void creer(String username, String plainPassword, String nom, String prenom, Role role) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Le nom d'utilisateur est obligatoire.");
        if (plainPassword == null || plainPassword.length() < 4)
            throw new IllegalArgumentException("Le mot de passe doit avoir au moins 4 caractères.");
        if (nom == null || nom.isBlank())
            throw new IllegalArgumentException("Le nom est obligatoire.");
        if (prenom == null || prenom.isBlank())
            throw new IllegalArgumentException("Le prénom est obligatoire.");
        if (role == null)
            throw new IllegalArgumentException("Le rôle est obligatoire.");
        if (dao.findByUsername(username.trim()).isPresent())
            throw new IllegalArgumentException("Ce nom d'utilisateur existe déjà.");

        Utilisateur u = new Utilisateur();
        u.setUsername(username.trim());
        u.setMotDePasseHash(BCrypt.hashpw(plainPassword, BCrypt.gensalt(12)));
        u.setNom(nom.trim());
        u.setPrenom(prenom.trim());
        u.setRole(role);
        u.setActif(true);
        dao.save(u);
    }

    public void modifierRole(Utilisateur u, Role nouveauRole) {
        u.setRole(nouveauRole);
        dao.update(u);
    }

    public void toggleActif(Utilisateur u) {
        u.setActif(!u.isActif());
        dao.update(u);
    }

    public void reinitialiserMotDePasse(Utilisateur u, String newPassword) {
        if (newPassword == null || newPassword.length() < 4)
            throw new IllegalArgumentException("Le mot de passe doit avoir au moins 4 caractères.");
        u.setMotDePasseHash(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
        dao.update(u);
    }

    public void supprimer(Utilisateur u) {
        if (u.getUsername().equals("admin"))
            throw new IllegalArgumentException("Impossible de supprimer le compte admin.");
        dao.delete(u);
    }
}
