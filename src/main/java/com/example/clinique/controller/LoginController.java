package com.example.clinique.controller;

import com.example.clinique.Repositorie.implementtion.UtilisateurDAOImple;
import com.example.clinique.service.AuthService;
import com.example.clinique.utilitaire.AlertUtil;
import com.example.clinique.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class LoginController {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblErreur;

    private final AuthService authService = new AuthService(new UtilisateurDAOImple());

    @FXML
    private void initialize() {
        lblErreur.setVisible(false);
        txtPassword.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) Login(); });
    }

    @FXML
    public void Login() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            afficherErreur("Veuillez saisir vos identifiants.");
            return;
        }

        if (authService.login(username, password)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/example/clinique/views/layout.fxml"));
                Scene scene = new Scene(root);
                HelloApplication.primaryStage.setScene(scene);
            } catch (Exception e) {
                AlertUtil.erreur("Erreur", e.getMessage());
            }
        } else {
            afficherErreur("Identifiants incorrects. Veuillez réessayer.");
            txtPassword.clear();
        }
    }

    private void afficherErreur(String msg) {
        lblErreur.setText(msg);
        lblErreur.setVisible(true);
    }
}
