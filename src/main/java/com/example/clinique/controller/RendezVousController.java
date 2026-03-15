package com.example.clinique.controller;

import com.example.clinique.Repositorie.implementtion.MedecinDAOImple;
import com.example.clinique.Repositorie.implementtion.PatientDAOImple;
import com.example.clinique.Repositorie.implementtion.RendezVousDAOImple;
import com.example.clinique.model.Medecin;
import com.example.clinique.model.Patient;
import com.example.clinique.model.RendezVous;
import com.example.clinique.model.enums.StatutRendezVous;
import com.example.clinique.service.MedecinService;
import com.example.clinique.service.PatientService;
import com.example.clinique.service.RendezVousService;
import com.example.clinique.utilitaire.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class RendezVousController {

    @FXML
    private ComboBox<Patient> cmbPatient;
    @FXML
    private ComboBox<Medecin> cmbMedecin;
    @FXML
    private DatePicker dpDate;
    @FXML
    private TextField txtHeure;
    @FXML
    private TextArea txtMotif;
    @FXML
    private ComboBox<StatutRendezVous> cmbStatut;
    @FXML
    private DatePicker dpFiltreDate;
    @FXML
    private ComboBox<Medecin> cmbFiltreMedecin;

    @FXML
    private TableColumn<RendezVous, Long> colId;
    @FXML
    private TableColumn<RendezVous, String> colPatient;
    @FXML
    private TableColumn<RendezVous, String> colMedecin;
    @FXML
    private TableColumn<RendezVous, String> colDate;
    @FXML
    private TableColumn<RendezVous, String> colMotif;
    @FXML
    private TableColumn<RendezVous, String> colStatut;

    @FXML
    private TableView<RendezVous> tableRdv;
    @FXML
    private ObservableList<RendezVous> rendezVous;
    @FXML
    private Label lblTitre;
    @FXML
    private Button btnSupprimer;

    private RendezVousDAOImple rdvDAO = new RendezVousDAOImple();
    private final RendezVousService rdvService = new RendezVousService(rdvDAO);
    private final PatientService patientService = new PatientService(new PatientDAOImple());
    private final MedecinService medecinService = new MedecinService(new MedecinDAOImple());
    private RendezVous rdvSelectionne = null;

    @FXML
    private void initialize() {
        if (colId == null) return;

        cmbPatient.setItems(FXCollections.observableArrayList(patientService.lister()));
        cmbMedecin.setItems(FXCollections.observableArrayList(medecinService.lister()));
        cmbFiltreMedecin.setItems(FXCollections.observableArrayList(medecinService.lister()));

        // Afficher nom complet dans les ComboBox
        StringConverter<Patient> patientConverter = new StringConverter<>() {
            public String toString(Patient p) { return p == null ? "" : p.getNomComplet(); }
            public Patient fromString(String s) { return null; }
        };
        cmbPatient.setConverter(patientConverter);
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

        StringConverter<Medecin> medecinConverter = new StringConverter<>() {
            public String toString(Medecin m) { return m == null ? "" : m.getNomComplet(); }
            public Medecin fromString(String s) { return null; }
        };
        cmbMedecin.setConverter(medecinConverter);
        cmbFiltreMedecin.setConverter(medecinConverter);
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
        cmbFiltreMedecin.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(Medecin m, boolean empty) {
                super.updateItem(m, empty);
                setText(empty || m == null ? null : m.getNomComplet());
            }
        });
        cmbFiltreMedecin.setButtonCell(new ListCell<>() {
            protected void updateItem(Medecin m, boolean empty) {
                super.updateItem(m, empty);
                setText(empty || m == null ? null : m.getNomComplet());
            }
        });
        cmbStatut.setItems(FXCollections.observableArrayList(StatutRendezVous.values()));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatient.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPatient().getNomComplet()));
        colMedecin.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMedecin().getNomComplet()));
        colDate.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDateHeure().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        colMotif.setCellValueFactory(new PropertyValueFactory<>("motif"));
        colStatut.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatut().toString()));

        chargerTableau();

        tableRdv.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, sel) -> { if (sel != null) remplirFormulaire(sel); });
    }

    @FXML
    public void addRendezVous() {
        try {
            LocalTime heure = LocalTime.parse(txtHeure.getText().trim(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime dateHeure = LocalDateTime.of(dpDate.getValue(), heure);

            RendezVous rv = new RendezVous();
            rv.setPatient(cmbPatient.getValue());
            rv.setMedecin(cmbMedecin.getValue());
            rv.setDateHeure(dateHeure);
            rv.setMotif(txtMotif.getText());
            rv.setStatut(cmbStatut.getValue() != null ? cmbStatut.getValue() : StatutRendezVous.PROGRAMME);

            if (rdvSelectionne == null) {
                rdvService.planifier(rv);
                AlertUtil.info("Succès", "Rendez-vous planifié.");
            } else {
                rv.setId(rdvSelectionne.getId());
                rdvService.modifier(rv);
                AlertUtil.info("Succès", "Rendez-vous modifié.");
            }
            clearFormulaire();
            chargerTableau();
        } catch (Exception e) {
            AlertUtil.erreur("Erreur", e.getMessage());
        }
    }

    @FXML
    public void annulerRdv() {
        if (rdvSelectionne == null) return;
        if (AlertUtil.confirmer("Confirmation", "Annuler ce rendez-vous ?")) {
            rdvService.annuler(rdvSelectionne);
            clearFormulaire();
            chargerTableau();
        }
    }

    @FXML
    public void filtrerAujourdhui() {
        rendezVous = FXCollections.observableArrayList(rdvService.listerAujourdhui());
        tableRdv.setItems(rendezVous);
    }

    @FXML
    public void filtrerParDate() {
        if (dpFiltreDate.getValue() != null) {
            rendezVous = FXCollections.observableArrayList(rdvService.listerParDate(dpFiltreDate.getValue()));
            tableRdv.setItems(rendezVous);
        }
    }

    @FXML
    public void filtrerParMedecin() {
        if (cmbFiltreMedecin.getValue() != null) {
            rendezVous = FXCollections.observableArrayList(rdvService.listerParMedecin(cmbFiltreMedecin.getValue()));
            tableRdv.setItems(rendezVous);
        }
    }

    @FXML
    public void reinitialiserFiltres() { chargerTableau(); }

    @FXML
    public void nouveauRdv() { clearFormulaire(); }

    private void chargerTableau() {
        rendezVous = FXCollections.observableArrayList(rdvService.lister());
        tableRdv.setItems(rendezVous);
    }

    private void remplirFormulaire(RendezVous rv) {
        rdvSelectionne = rv;
        lblTitre.setText("Modifier Rendez-vous");
        cmbPatient.setValue(rv.getPatient());
        cmbMedecin.setValue(rv.getMedecin());
        dpDate.setValue(rv.getDateHeure().toLocalDate());
        txtHeure.setText(rv.getDateHeure().format(DateTimeFormatter.ofPattern("HH:mm")));
        txtMotif.setText(rv.getMotif() != null ? rv.getMotif() : "");
        cmbStatut.setValue(rv.getStatut());
        btnSupprimer.setDisable(false);
    }

    private void clearFormulaire() {
        rdvSelectionne = null;
        cmbPatient.setValue(null); cmbMedecin.setValue(null);
        dpDate.setValue(null); txtHeure.clear(); txtMotif.clear();
        cmbStatut.setValue(StatutRendezVous.PROGRAMME);
        lblTitre.setText("Nouveau Rendez-vous");
        btnSupprimer.setDisable(true);
    }
}