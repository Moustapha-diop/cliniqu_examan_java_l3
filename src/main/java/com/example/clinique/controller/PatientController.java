package com.example.clinique.controller;

import com.example.clinique.Repositorie.IPatientDAO;
import com.example.clinique.Repositorie.implementtion.PatientDAOImple;
import com.example.clinique.model.Patient;
import com.example.clinique.model.enums.GroupeSanguin;
import com.example.clinique.model.enums.Sexe;
import com.example.clinique.service.PatientService;
import com.example.clinique.utilitaire.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;

public class PatientController {

    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private DatePicker dateNaissance;
    @FXML
    private ComboBox<Sexe> sexe;
    @FXML
    private TextField telephone;
    @FXML
    private TextField adresse;
    @FXML
    private ComboBox<GroupeSanguin> groupeSanguin;
    @FXML
    private TextArea antecedents;
    @FXML
    private TextField txtSearch;

    @FXML
    private TableColumn<Patient, Long> colId;
    @FXML
    private TableColumn<Patient, String> colNom;
    @FXML
    private TableColumn<Patient, String> colPrenom;
    @FXML
    private TableColumn<Patient, String> colDateNaissance;
    @FXML
    private TableColumn<Patient, String> colSexe;
    @FXML
    private TableColumn<Patient, String> colTelephone;
    @FXML
    private TableColumn<Patient, String> colGroupeSanguin;

    @FXML
    private TableView<Patient> tablePatients;
    @FXML
    private ObservableList<Patient> patients;
    @FXML
    private Label lblTitre;
    @FXML
    private Button btnSupprimer;

    private IPatientDAO iPatientDAO = new PatientDAOImple();
    private final PatientService patientService = new PatientService(iPatientDAO);
    private Patient patientSelectionne = null;

    @FXML
    private void initialize() {
        if (colId == null) return;

        sexe.setItems(FXCollections.observableArrayList(Sexe.values()));
        groupeSanguin.setItems(FXCollections.observableArrayList(GroupeSanguin.values()));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colDateNaissance.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getDateNaissance()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        colSexe.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getSexe().toString()));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colGroupeSanguin.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getGroupeSanguin() != null
                ? cell.getValue().getGroupeSanguin().getLabel() : "-"));

        chargerTableau();

        tablePatients.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, selected) -> { if (selected != null) remplirFormulaire(selected); });

        txtSearch.textProperty().addListener((obs, old, val) -> rechercher());
    }

    @FXML
    public void addPatient() {
        try {
            Patient p = new Patient();
            p.setNom(nom.getText());
            p.setPrenom(prenom.getText());
            p.setDateNaissance(dateNaissance.getValue());
            p.setSexe(sexe.getValue());
            p.setTelephone(telephone.getText());
            p.setAdresse(adresse.getText());
            p.setGroupeSanguin(groupeSanguin.getValue());
            p.setAntecedentsMedicaux(antecedents.getText());

            if (patientSelectionne == null) {
                patientService.ajouter(p);
                AlertUtil.info("Succès", "Patient ajouté avec succès.");
            } else {
                p.setId(patientSelectionne.getId());
                patientService.modifier(p);
                AlertUtil.info("Succès", "Patient modifié avec succès.");
            }
            clearFormulaire();
            chargerTableau();
        } catch (Exception e) {
            AlertUtil.erreur("Erreur", e.getMessage());
        }
    }

    @FXML
    public void deletePatient() {
        if (patientSelectionne == null) return;
        if (AlertUtil.confirmer("Confirmation", "Supprimer ce patient ?")) {
            patientService.supprimer(patientSelectionne);
            clearFormulaire();
            chargerTableau();
        }
    }

    @FXML
    public void nouveauPatient() {
        clearFormulaire();
    }

    @FXML
    private void rechercher() {
        String keyword = txtSearch.getText();
        patients = FXCollections.observableArrayList(patientService.rechercher(keyword));
        tablePatients.setItems(patients);
    }

    private void chargerTableau() {
        patients = FXCollections.observableArrayList(patientService.lister());
        tablePatients.setItems(patients);
    }

    private void remplirFormulaire(Patient p) {
        patientSelectionne = p;
        lblTitre.setText("Modifier Patient");
        nom.setText(p.getNom());
        prenom.setText(p.getPrenom());
        dateNaissance.setValue(p.getDateNaissance());
        sexe.setValue(p.getSexe());
        telephone.setText(p.getTelephone());
        adresse.setText(p.getAdresse() != null ? p.getAdresse() : "");
        groupeSanguin.setValue(p.getGroupeSanguin());
        antecedents.setText(p.getAntecedentsMedicaux() != null ? p.getAntecedentsMedicaux() : "");
        btnSupprimer.setDisable(false);
    }

    private void clearFormulaire() {
        patientSelectionne = null;
        nom.clear(); prenom.clear(); telephone.clear(); adresse.clear(); antecedents.clear();
        dateNaissance.setValue(null); sexe.setValue(null); groupeSanguin.setValue(null);
        lblTitre.setText("Nouveau Patient");
        btnSupprimer.setDisable(true);
    }
}
