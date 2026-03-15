package com.example.clinique.service;

import com.example.clinique.Repositorie.IRendezVousDAO;
import com.example.clinique.model.Medecin;
import com.example.clinique.model.RendezVous;
import com.example.clinique.model.enums.StatutRendezVous;

import java.time.LocalDate;
import java.util.List;

public class RendezVousService {

    private final IRendezVousDAO dao;

    public RendezVousService(IRendezVousDAO dao) {
        this.dao = dao;
    }

    public void planifier(RendezVous rv) {
        valider(rv);
        if (dao.isMedecinOccupe(rv.getMedecin(), rv.getDateHeure(), null))
            throw new IllegalStateException("Le médecin a déjà un rendez-vous à cet horaire.");
        dao.save(rv);
    }

    public void modifier(RendezVous rv) {
        valider(rv);
        if (dao.isMedecinOccupe(rv.getMedecin(), rv.getDateHeure(), rv.getId()))
            throw new IllegalStateException("Le médecin a déjà un rendez-vous à cet horaire.");
        dao.update(rv);
    }

    public void annuler(RendezVous rv) {
        rv.setStatut(StatutRendezVous.ANNULE);
        dao.update(rv);
    }

    public void terminer(RendezVous rv) {
        rv.setStatut(StatutRendezVous.TERMINE);
        dao.update(rv);
    }

    public List<RendezVous> lister() {
        return dao.getAll();
    }

    public List<RendezVous> listerParDate(LocalDate date) {
        return dao.findByDate(date);
    }

    public List<RendezVous> listerAujourdhui() {
        return dao.findByDate(LocalDate.now());
    }

    public List<RendezVous> listerParMedecin(Medecin medecin) {
        return dao.findByMedecin(medecin);
    }

    private void valider(RendezVous rv) {
        if (rv.getPatient() == null) throw new IllegalArgumentException("Le patient est obligatoire.");
        if (rv.getMedecin() == null) throw new IllegalArgumentException("Le médecin est obligatoire.");
        if (rv.getDateHeure() == null) throw new IllegalArgumentException("La date/heure est obligatoire.");
    }
}
