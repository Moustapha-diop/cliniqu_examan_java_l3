package com.example.clinique.controller;

import com.example.clinique.Repositorie.implementtion.*;
import com.example.clinique.model.Consultation;
import com.example.clinique.model.Medecin;
import com.example.clinique.model.Patient;
import com.example.clinique.model.RendezVous;
import com.example.clinique.model.enums.StatutRendezVous;
import com.example.clinique.service.ConsultationService;
import com.example.clinique.service.MedecinService;
import com.example.clinique.service.PatientService;
import com.example.clinique.service.RendezVousService;
import com.example.clinique.utilitaire.AlertUtil;
import com.example.clinique.utilitaire.PDFGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class ConsultationController {

    @FXML
    private ComboBox<Patient> cmbPatient;
    @FXML
    private ComboBox<Medecin> cmbMedecin;
    @FXML
    private ComboBox<RendezVous> cmbRendezVous;
    @FXML
    private TextArea txtDiagnostic;
    @FXML
    private TextArea txtObservations;
    @FXML
    private TextArea txtPrescription;

    @FXML
    private TableColumn<Consultation, Long> colId;
    @FXML
    private TableColumn<Consultation, String> colPatient;
    @FXML
    private TableColumn<Consultation, String> colMedecin;
    @FXML
    private TableColumn<Consultation, String> colDate;
    @FXML
    private TableColumn<Consultation, String> colDiagnostic;

    @FXML
    private TableView<Consultation> tableConsultations;
    @FXML
    private ObservableList<Consultation> consultations;
    @FXML
    private Label lblTitre;

    private RendezVousDAOImple rdvDAO = new RendezVousDAOImple();
    private ConsultationDAOImple consultationDAO = new ConsultationDAOImple();
    private final ConsultationService consultationService = new ConsultationService(consultationDAO, rdvDAO);
    private final PatientService patientService = new PatientService(new PatientDAOImple());
    private final MedecinService medecinService = new MedecinService(new MedecinDAOImple());
    private final RendezVousService rdvService = new RendezVousService(rdvDAO);

    private Consultation consultationSelectionnee = null;

    @FXML
    private void initialize() {
        if (colId == null) return;

        cmbPatient.setItems(FXCollections.observableArrayList(patientService.lister()));
        cmbMedecin.setItems(FXCollections.observableArrayList(medecinService.lister()));

        // Afficher nom complet dans les ComboBox
        cmbPatient.setConverter(new StringConverter<>() {
            public String toString(Patient p) { return p == null ? "" : p.getNomComplet(); }
            public Patient fromString(String s) { return null; }
        });
        cmbPatient.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(Patient p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null : p.getNomComplet());
            }
        });
        cmbPatient.setButtonCell(new ListCell<>() {
            protected void updateItem(Patient p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null : p.getNomComplet());
            }
        });

        cmbMedecin.setConverter(new StringConverter<>() {
            public String toString(Medecin m) { return m == null ? "" : m.getNomComplet(); }
            public Medecin fromString(String s) { return null; }
        });
        cmbMedecin.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(Medecin m, boolean empty) {
                super.updateItem(m, empty);
                setText(empty || m == null ? null : m.getNomComplet());
            }
        });
        cmbMedecin.setButtonCell(new ListCell<>() {
            protected void updateItem(Medecin m, boolean empty) {
                super.updateItem(m, empty);
                setText(empty || m == null ? null : m.getNomComplet());
            }
        });

        cmbRendezVous.setConverter(new StringConverter<>() {
            public String toString(RendezVous rv) {
                if (rv == null) return "";
                return rv.getDateHeure().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                        + " — " + rv.getMedecin().getNomComplet();
            }
            public RendezVous fromString(String s) { return null; }
        });
        cmbRendezVous.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(RendezVous rv, boolean empty) {
                super.updateItem(rv, empty);
                if (empty || rv == null) { setText(null); return; }
                setText(rv.getDateHeure().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                        + " — " + rv.getMedecin().getNomComplet());
            }
        });
        cmbRendezVous.setButtonCell(new ListCell<>() {
            protected void updateItem(RendezVous rv, boolean empty) {
                super.updateItem(rv, empty);
                if (empty || rv == null) { setText(null); return; }
                setText(rv.getDateHeure().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                        + " — " + rv.getMedecin().getNomComplet());
            }
        });

        cmbPatient.valueProperty().addListener((obs, old, patient) -> {
            if (patient != null) {
                var rdvs = rdvService.lister().stream()
                        .filter(rv -> rv.getPatient().getId() == patient.getId()
                                && rv.getStatut() == StatutRendezVous.PROGRAMME)
                        .collect(Collectors.toList());
                cmbRendezVous.setItems(FXCollections.observableArrayList(rdvs));
            }
        });

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatient.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPatient().getNomComplet()));
        colMedecin.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMedecin().getNomComplet()));
        colDate.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDateConsultation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        colDiagnostic.setCellValueFactory(new PropertyValueFactory<>("diagnostic"));

        chargerTableau();

        tableConsultations.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, sel) -> { if (sel != null) remplirFormulaire(sel); });
    }

    @FXML
    public void addConsultation() {
        try {
            Consultation c = new Consultation();
            c.setPatient(cmbPatient.getValue());
            c.setMedecin(cmbMedecin.getValue());
            c.setRendezVous(cmbRendezVous.getValue());
            c.setDateConsultation(LocalDateTime.now());
            c.setDiagnostic(txtDiagnostic.getText());
            c.setObservations(txtObservations.getText());
            c.setPrescription(txtPrescription.getText());

            if (consultationSelectionnee == null) {
                consultationService.enregistrer(c);
                AlertUtil.info("Succès", "Consultation enregistrée.");
            } else {
                c.setId(consultationSelectionnee.getId());
                consultationService.modifier(c);
                AlertUtil.info("Succès", "Consultation modifiée.");
            }
            clearFormulaire();
            chargerTableau();
        } catch (Exception e) {
            AlertUtil.erreur("Erreur", e.getMessage());
        }
    }

    @FXML
    public void genererOrdonnance() {
        if (consultationSelectionnee == null) {
            AlertUtil.erreur("Erreur", "Sélectionnez une consultation.");
            return;
        }
        FileChooser fc = new FileChooser();
        fc.setTitle("Enregistrer l'ordonnance PDF");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        fc.setInitialFileName("ordonnance_" + consultationSelectionnee.getId() + ".pdf");
        Window window = tableConsultations.getScene().getWindow();
        File file = fc.showSaveDialog(window);
        if (file != null) {
            try {
                PDFGenerator.genererOrdonnance(consultationSelectionnee, file.getAbsolutePath());
                AlertUtil.info("Succès", "Ordonnance PDF générée : " + file.getName());
            } catch (Exception e) {
                AlertUtil.erreur("Erreur PDF", e.getMessage());
            }
        }
    }

    private void chargerTableau() {
        consultations = FXCollections.observableArrayList(consultationService.lister());
        tableConsultations.setItems(consultations);
    }

    private void remplirFormulaire(Consultation c) {
        consultationSelectionnee = c;
        lblTitre.setText("Modifier Consultation");
        cmbPatient.setValue(c.getPatient());
        cmbMedecin.setValue(c.getMedecin());
        cmbRendezVous.setValue(c.getRendezVous());
        txtDiagnostic.setText(c.getDiagnostic() != null ? c.getDiagnostic() : "");
        txtObservations.setText(c.getObservations() != null ? c.getObservations() : "");
        txtPrescription.setText(c.getPrescription() != null ? c.getPrescription() : "");
    }

    private void clearFormulaire() {
        consultationSelectionnee = null;
        cmbPatient.setValue(null); cmbMedecin.setValue(null); cmbRendezVous.setValue(null);
        txtDiagnostic.clear(); txtObservations.clear(); txtPrescription.clear();
        lblTitre.setText("Nouvelle Consultation");
    }
}