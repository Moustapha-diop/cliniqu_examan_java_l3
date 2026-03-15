package com.example.clinique.utilitaire;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class Navigation {

    public static void loadView(String fxml, StackPane stackPane) {
        try {
            Parent view = FXMLLoader.load(
                Navigation.class.getResource("/com/example/clinique/views/" + fxml)
            );
            stackPane.getChildren().setAll(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
