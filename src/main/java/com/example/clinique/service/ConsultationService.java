package com.example.clinique.service;

import com.example.clinique.Repositorie.IConsultationDAO;
import com.example.clinique.Repositorie.IRendezVousDAO;
import com.example.clinique.model.Consultation;
import com.example.clinique.model.RendezVous;
import com.example.clinique.model.enums.StatutRendezVous;

import java.time.LocalDateTime;
import java.util.List;

public class ConsultationService {

    private final IConsultationDAO dao;
    private final IRendezVousDAO rendezVousDAO;

    public ConsultationService(IConsultationDAO dao, IRendezVousDAO rendezVousDAO) {
        this.dao = dao;
        this.rendezVousDAO = rendezVousDAO;
    }

    public void enregistrer(Consultation consultation) {
        valider(consultation);
        if (consultation.getDateConsultation() == null)
            consultation.setDateConsultation(LocalDateTime.now());
        dao.save(consultation);
        // Marquer le RDV comme terminé
        RendezVous rv = consultation.getRendezVous();
        if (rv != null) {
            rv.setStatut(StatutRendezVous.TERMINE);
            rendezVousDAO.update(rv);
        }
    }

    public void modifier(Consultation consultation) {
        valider(consultation);
        dao.update(consultation);
    }

    public List<Consultation> lister() {
        return dao.getAll();
    }

    public List<Consultation> listerParPatient(long patientId) {
        return dao.findByPatientId(patientId);
    }

    private void valider(Consultation c) {
        if (c.getPatient() == null) throw new IllegalArgumentException("Le patient est obligatoire.");
        if (c.getMedecin() == null) throw new IllegalArgumentException("Le médecin est obligatoire.");
        if (c.getDiagnostic() == null || c.getDiagnostic().isBlank())
            throw new IllegalArgumentException("Le diagnostic est obligatoire.");
    }
}
