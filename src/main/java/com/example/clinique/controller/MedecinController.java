package com.example.clinique.controller;

import com.example.clinique.Repositorie.IMedecinDAO;
import com.example.clinique.Repositorie.implementtion.MedecinDAOImple;
import com.example.clinique.model.Medecin;
import com.example.clinique.service.MedecinService;
import com.example.clinique.utilitaire.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class MedecinController {

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private TextField txtSpecialite;
    @FXML
    private TextField txtTelephone;

    @FXML
    private TableColumn<Medecin, Long> colId;
    @FXML
    private TableColumn<Medecin, String> colNom;
    @FXML
    private TableColumn<Medecin, String> colPrenom;
    @FXML
    private TableColumn<Medecin, String> colSpecialite;
    @FXML
    private TableColumn<Medecin, String> colTelephone;

    @FXML
    private TableView<Medecin> tableMedecins;
    @FXML
    private ObservableList<Medecin> medecins;
    @FXML
    private Label lblTitre;
    @FXML
    private Button btnSupprimer;

    private IMedecinDAO iMedecinDAO = new MedecinDAOImple();
    private final MedecinService medecinService = new MedecinService(iMedecinDAO);
    private Medecin medecinSelectionne = null;

    @FXML
    private void initialize() {
        if (colId == null) return;

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colSpecialite.setCellValueFactory(new PropertyValueFactory<>("specialite"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        chargerTableau();

        tableMedecins.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, sel) -> { if (sel != null) remplirFormulaire(sel); });
    }

    @FXML
    public void addMedecin() {
        try {
            Medecin m = new Medecin();
            m.setNom(txtNom.getText());
            m.setPrenom(txtPrenom.getText());
            m.setSpecialite(txtSpecialite.getText());
            m.setTelephone(txtTelephone.getText());

            if (medecinSelectionne == null) {
                medecinService.ajouter(m);
                AlertUtil.info("Succès", "Médecin ajouté.");
            } else {
                m.setId(medecinSelectionne.getId());
                medecinService.modifier(m);
                AlertUtil.info("Succès", "Médecin modifié.");
            }
            clearFormulaire();
            chargerTableau();
        } catch (Exception e) {
            AlertUtil.erreur("Erreur", e.getMessage());
        }
    }

    @FXML
    public void deleteMedecin() {
        if (medecinSelectionne == null) return;
        if (AlertUtil.confirmer("Confirmation", "Supprimer ce médecin ?")) {
            medecinService.supprimer(medecinSelectionne);
            clearFormulaire();
            chargerTableau();
        }
    }

    @FXML
    public void nouveauMedecin() { clearFormulaire(); }

    private void chargerTableau() {
        medecins = FXCollections.observableArrayList(medecinService.lister());
        tableMedecins.setItems(medecins);
    }

    private void remplirFormulaire(Medecin m) {
        medecinSelectionne = m;
        lblTitre.setText("Modifier Médecin");
        txtNom.setText(m.getNom());
        txtPrenom.setText(m.getPrenom());
        txtSpecialite.setText(m.getSpecialite());
        txtTelephone.setText(m.getTelephone() != null ? m.getTelephone() : "");
        btnSupprimer.setDisable(false);
    }

    private void clearFormulaire() {
        medecinSelectionne = null;
        txtNom.clear(); txtPrenom.clear(); txtSpecialite.clear(); txtTelephone.clear();
        lblTitre.setText("Nouveau Médecin");
        btnSupprimer.setDisable(true);
    }
}
