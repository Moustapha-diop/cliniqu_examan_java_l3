package com.example.clinique.controller;

import com.example.clinique.Repositorie.implementtion.UtilisateurDAOImple;
import com.example.clinique.model.Utilisateur;
import com.example.clinique.model.enums.Role;
import com.example.clinique.service.UtilisateurService;
import com.example.clinique.utilitaire.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class UtilisateurController {

    // ── Tableau ──
    @FXML
    private TableView<Utilisateur> tableUtilisateurs;
    @FXML
    private TableColumn<Utilisateur, Long>    colId;
    @FXML
    private TableColumn<Utilisateur, String>  colUsername;
    @FXML
    private TableColumn<Utilisateur, String>  colNom;
    @FXML
    private TableColumn<Utilisateur, String>  colPrenom;
    @FXML
    private TableColumn<Utilisateur, Role>    colRole;
    @FXML
    private TableColumn<Utilisateur, Boolean> colActif;

    // ── Formulaire ──
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private ComboBox<Role> cmbRole;

    // ── Réinitialisation mot de passe ──
    @FXML private PasswordField txtNouveauMdp;

    private final UtilisateurService service =
            new UtilisateurService(new UtilisateurDAOImple());

    private ObservableList<Utilisateur> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colActif.setCellValueFactory(new PropertyValueFactory<>("actif"));

        // Afficher Oui/Non pour actif
        colActif.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : (item ? "Actif" : "Inactif"));
            }
        });

        cmbRole.setItems(FXCollections.observableArrayList(Role.values()));

        chargerDonnees();

        // Remplir le formulaire au clic
        tableUtilisateurs.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, selected) -> {
                if (selected != null) {
                    txtUsername.setText(selected.getUsername());
                    txtNom.setText(selected.getNom());
                    txtPrenom.setText(selected.getPrenom());
                    cmbRole.setValue(selected.getRole());
                    txtPassword.clear();
                    txtNouveauMdp.clear();
                }
            }
        );
    }

    private void chargerDonnees() {
        data.setAll(service.getAll());
        tableUtilisateurs.setItems(data);
    }

    @FXML
    private void Ajouter() {
        try {
            service.creer(
                txtUsername.getText(),
                txtPassword.getText(),
                txtNom.getText(),
                txtPrenom.getText(),
                cmbRole.getValue()
            );
            AlertUtil.info("Succès", "Utilisateur créé avec succès !");
            viderFormulaire();
            chargerDonnees();
        } catch (Exception e) {
            AlertUtil.erreur("Erreur", e.getMessage());
        }
    }

    @FXML
    private void ToggleActif() {
        Utilisateur selected = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertUtil.avertissement("Attention", "Sélectionnez un utilisateur."); return; }
        try {
            service.toggleActif(selected);
            chargerDonnees();
        } catch (Exception e) {
            AlertUtil.erreur("Erreur", e.getMessage());
        }
    }

    @FXML
    private void ReinitialiserMdp() {
        Utilisateur selected = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertUtil.avertissement("Attention", "Sélectionnez un utilisateur."); return; }
        try {
            service.reinitialiserMotDePasse(selected, txtNouveauMdp.getText());
            AlertUtil.info("Succès", "Mot de passe réinitialisé !");
            txtNouveauMdp.clear();
            chargerDonnees();
        } catch (Exception e) {
            AlertUtil.erreur("Erreur", e.getMessage());
        }
    }

    @FXML
    private void Supprimer() {
        Utilisateur selected = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertUtil.avertissement("Attention", "Sélectionnez un utilisateur."); return; }
        if (!AlertUtil.confirmer("Confirmation", "Supprimer l'utilisateur " + selected.getUsername() + " ?")) return;
        try {
            service.supprimer(selected);
            viderFormulaire();
            chargerDonnees();
        } catch (Exception e) {
            AlertUtil.erreur("Erreur", e.getMessage());
        }
    }

    @FXML
    private void NouveauFormulaire() {
        viderFormulaire();
    }

    private void viderFormulaire() {
        txtUsername.clear();
        txtPassword.clear();
        txtNom.clear();
        txtPrenom.clear();
        cmbRole.setValue(null);
        txtNouveauMdp.clear();
        tableUtilisateurs.getSelectionModel().clearSelection();
    }
}
