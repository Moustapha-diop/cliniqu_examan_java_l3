package com.example.clinique.controller;

import com.example.clinique.Repositorie.implementtion.ConsultationDAOImple;
import com.example.clinique.Repositorie.implementtion.FactureDAOImple;
import com.example.clinique.model.Consultation;
import com.example.clinique.model.Facture;
import com.example.clinique.model.enums.StatutPaiement;
import com.example.clinique.service.ConsultationService;
import com.example.clinique.service.FactureService;
import com.example.clinique.Repositorie.implementtion.RendezVousDAOImple;
import com.example.clinique.utilitaire.AlertUtil;
import com.example.clinique.utilitaire.PDFGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class FacturationController {

    @FXML
    private ComboBox<Consultation> cmbConsultation;
    @FXML
    private TextField txtMontant;
    @FXML
    private ComboBox<com.example.clinique.model.enums.ModePaiement> cmbModePaiement;

    @FXML
    private TableColumn<Facture, Long> colId;
    @FXML
    private TableColumn<Facture, String> colNumero;
    @FXML
    private TableColumn<Facture, String> colPatient;
    @FXML
    private TableColumn<Facture, String> colDate;
    @FXML
    private TableColumn<Facture, Double> colMontant;
    @FXML
    private TableColumn<Facture, String> colMode;
    @FXML
    private TableColumn<Facture, String> colStatut;

    @FXML
    private TableView<Facture> tableFactures;
    @FXML
    private ObservableList<Facture> factures;
    @FXML
    private Label lblTotalPayees;
    @FXML
    private Label lblTotalNonPayees;

    private FactureDAOImple factureDAO = new FactureDAOImple();
    private ConsultationDAOImple consultationDAO = new ConsultationDAOImple();
    private final FactureService factureService = new FactureService(factureDAO);
    private final ConsultationService consultationService = new ConsultationService(consultationDAO, new RendezVousDAOImple());
    private Facture factureSelectionnee = null;

    @FXML
    private void initialize() {
        if (colId == null) return;

        cmbConsultation.setItems(FXCollections.observableArrayList(consultationService.lister()));
        cmbConsultation.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(Consultation c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) { setText(null); return; }
                setText(c.getDateConsultation().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                        + " — " + c.getPatient().getNomComplet());
            }
        });
        cmbConsultation.setButtonCell(new ListCell<>() {
            protected void updateItem(Consultation c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) { setText(null); return; }
                setText(c.getDateConsultation().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                        + " — " + c.getPatient().getNomComplet());
            }
        });
        cmbModePaiement.setItems(FXCollections.observableArrayList(com.example.clinique.model.enums.ModePaiement.values()));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNumero.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNumeroFacture()));
        colPatient.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getConsultation().getPatient().getNomComplet()));
        colDate.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDateFacture().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        colMode.setCellValueFactory(new PropertyValueFactory<>("modePaiement"));
        colStatut.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatut().toString()));

        chargerTableau();

        tableFactures.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, sel) -> { if (sel != null) factureSelectionnee = sel; });
    }

    @FXML
    public void genererFacture() {
        try {
            double montant = Double.parseDouble(txtMontant.getText().trim());
            factureService.generer(cmbConsultation.getValue(), montant, cmbModePaiement.getValue());
            AlertUtil.info("Succès", "Facture générée.");
            chargerTableau();
            clearFormulaire();
        } catch (NumberFormatException e) {
            AlertUtil.erreur("Erreur", "Montant invalide.");
        } catch (Exception e) {
            AlertUtil.erreur("Erreur", e.getMessage());
        }
    }

    @FXML
    public void marquerPayee() {
        if (factureSelectionnee == null) { AlertUtil.erreur("Erreur", "Sélectionnez une facture."); return; }
        factureService.marquerPayee(factureSelectionnee);
        chargerTableau();
        AlertUtil.info("Succès", "Facture marquée comme payée.");
    }

    @FXML
    public void exporterPDF() {
        if (factureSelectionnee == null) { AlertUtil.erreur("Erreur", "Sélectionnez une facture."); return; }
        FileChooser fc = new FileChooser();
        fc.setTitle("Enregistrer la facture PDF");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        fc.setInitialFileName(factureSelectionnee.getNumeroFacture() + ".pdf");
        Window window = tableFactures.getScene().getWindow();
        File file = fc.showSaveDialog(window);
        if (file != null) {
            try {
                PDFGenerator.genererFacture(factureSelectionnee, file.getAbsolutePath());
                AlertUtil.info("Succès", "Facture PDF générée.");
            } catch (Exception e) {
                AlertUtil.erreur("Erreur PDF", e.getMessage());
            }
        }
    }

    @FXML
    public void filtrerPayees() {
        factures = FXCollections.observableArrayList(factureService.listerParStatut(StatutPaiement.PAYE));
        tableFactures.setItems(factures);
    }

    @FXML
    public void filtrerNonPayees() {
        factures = FXCollections.observableArrayList(factureService.listerParStatut(StatutPaiement.NON_PAYE));
        tableFactures.setItems(factures);
    }

    @FXML
    public void afficherToutes() { chargerTableau(); }

    private void chargerTableau() {
        factures = FXCollections.observableArrayList(factureService.lister());
        tableFactures.setItems(factures);
        majStats();
    }

    private void majStats() {
        lblTotalPayees.setText(String.valueOf(factureService.listerParStatut(StatutPaiement.PAYE).size()));
        lblTotalNonPayees.setText(String.valueOf(factureService.listerParStatut(StatutPaiement.NON_PAYE).size()));
    }

    private void clearFormulaire() {
        cmbConsultation.setValue(null);
        txtMontant.clear();
        cmbModePaiement.setValue(null);
        factureSelectionnee = null;
    }
}