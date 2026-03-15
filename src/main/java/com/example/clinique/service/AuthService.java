package com.example.clinique.service;

import com.example.clinique.Repositorie.IUtilisateurDAO;
import com.example.clinique.model.Utilisateur;
import com.example.clinique.model.enums.Role;
import com.example.clinique.session.SessionUtilisateur;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthService {

    private final IUtilisateurDAO dao;

    public AuthService(IUtilisateurDAO dao) {
        this.dao = dao;
    }

    public boolean login(String username, String plainPassword) {
        if (username == null || username.isBlank() || plainPassword == null || plainPassword.isBlank())
            return false;

        Optional<Utilisateur> opt = dao.findByUsername(username.trim());
        if (opt.isEmpty()) return false;

        Utilisateur user = opt.get();
        if (!user.isActif()) return false;
        if (!BCrypt.checkpw(plainPassword, user.getMotDePasseHash())) return false;

        SessionUtilisateur.getInstance().ouvrirSession(user);
        return true;
    }

    public void logout() {
        SessionUtilisateur.getInstance().fermerSession();
    }

    public void initAdminSiNecessaire() {
        if (dao.findByUsername("admin").isEmpty()) {
            Utilisateur admin = new Utilisateur();
            admin.setUsername("admin");
            admin.setMotDePasseHash(BCrypt.hashpw("admin123", BCrypt.gensalt(12)));
            admin.setNom("Admin");
            admin.setPrenom("Super");
            admin.setRole(Role.ADMIN);
            admin.setActif(true);
            dao.save(admin);
        }
    }
}
