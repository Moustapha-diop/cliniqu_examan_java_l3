package com.example.clinique.controller;

import com.example.clinique.Repositorie.implementtion.UtilisateurDAOImple;
import com.example.clinique.HelloApplication;
import com.example.clinique.model.enums.Role;
import com.example.clinique.service.AuthService;
import com.example.clinique.session.SessionUtilisateur;
import com.example.clinique.utilitaire.AlertUtil;
import com.example.clinique.utilitaire.Navigation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.StackPane;

public class HelloController {

    @FXML
    private StackPane stackPane;
    @FXML
    private Label lblUser;

    // Menus à cacher selon le rôle
    @FXML
    private Menu menuPatients;
    @FXML
    private Menu menuMedecins;
    @FXML
    private Menu menuRendezVous;
    @FXML
    private Menu menuConsultations;
    @FXML
    private Menu menuFacturation;
    @FXML
    private Menu menuUtilisateurs;

    @FXML
    private void initialize() {
        SessionUtilisateur session = SessionUtilisateur.getInstance();
        Role role = session.getRole();

        lblUser.setText(session.getNomComplet() + " (" + role + ")");

        // ── Restrictions par rôle ──
        switch (role) {
            case MEDECIN -> {
                // Médecin : uniquement ses RDV et consultations
                menuPatients.setVisible(false);
                menuMedecins.setVisible(false);
                menuFacturation.setVisible(false);
                menuUtilisateurs.setVisible(false);
            }
            case RECEPTIONNISTE -> {
                // Réceptionniste : patients + rendez-vous uniquement
                menuMedecins.setVisible(false);
                menuConsultations.setVisible(false);
                menuFacturation.setVisible(false);
                menuUtilisateurs.setVisible(false);
            }
            case ADMIN -> {
                // Admin : accès total — rien à cacher
            }
        }

        Navigation.loadView("dashboard.fxml", stackPane);
    }

    @FXML
    public void afficheDashboard()    { Navigation.loadView("dashboard.fxml",     stackPane); }
    @FXML
    public void affichePatients()     { Navigation.loadView("patient.fxml",        stackPane); }
    @FXML
    public void afficheMedecins()     { Navigation.loadView("medecin.fxml",        stackPane); }
    @FXML
    public void afficheRendezVous()   { Navigation.loadView("rendezvous.fxml",     stackPane); }
    @FXML
    public void afficheConsultations(){ Navigation.loadView("consultation.fxml",   stackPane); }
    @FXML
    public void afficheFacturation()  { Navigation.loadView("facturation.fxml",    stackPane); }
    @FXML
    void afficheUtilisateurs() { Navigation.loadView("utilisateur.fxml",    stackPane); }

    @FXML
    public void Logout() {
        try {
            new AuthService(new UtilisateurDAOImple()).logout();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/clinique/views/login.fxml"));
            HelloApplication.primaryStage.setScene(new Scene(root));
        } catch (Exception e) {
            AlertUtil.erreur("Erreur", e.getMessage());
        }
    }
}
