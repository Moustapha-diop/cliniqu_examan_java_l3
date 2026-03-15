package com.example.clinique;

import com.example.clinique.Repositorie.implementtion.UtilisateurDAOImple;
import com.example.clinique.config.FactoryJPA;
import com.example.clinique.service.AuthService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // Initialiser JPA
        FactoryJPA.getEmf();

        // Créer l'admin par défaut
        AuthService authService = new AuthService(new UtilisateurDAOImple());
        authService.initAdminSiNecessaire();

        // Charger le login en premier
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/clinique/views/login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Clinique Médicale — Système de Gestion");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    @Override
    public void stop() {
        FactoryJPA.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}