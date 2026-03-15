package com.example.clinique.session;

import com.example.clinique.model.Utilisateur;
import com.example.clinique.model.enums.Role;

public class SessionUtilisateur {

    private static SessionUtilisateur instance;

    private long userId;
    private String username;
    private String nomComplet;
    private Role role;
    private boolean authentifie = false;

    private SessionUtilisateur() {}

    public static SessionUtilisateur getInstance() {
        if (instance == null) instance = new SessionUtilisateur();
        return instance;
    }

    public void ouvrirSession(Utilisateur utilisateur) {
        this.userId = utilisateur.getId();
        this.username = utilisateur.getUsername();
        this.nomComplet = utilisateur.getPrenom() + " " + utilisateur.getNom();
        this.role = utilisateur.getRole();
        this.authentifie = true;
    }

    public void fermerSession() {
        this.userId = 0;
        this.username = null;
        this.nomComplet = null;
        this.role = null;
        this.authentifie = false;
    }

    public long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getNomComplet() { return nomComplet; }
    public Role getRole() { return role; }
    public boolean isAuthentifie() { return authentifie; }
    public boolean isAdmin() { return Role.ADMIN.equals(role); }
    public boolean isMedecin() { return Role.MEDECIN.equals(role); }
    public boolean isReceptionniste() { return Role.RECEPTIONNISTE.equals(role); }
}
