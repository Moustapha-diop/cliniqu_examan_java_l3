package com.example.clinique.service;

import com.example.clinique.Repositorie.IMedecinDAO;
import com.example.clinique.model.Medecin;

import java.util.List;

public class MedecinService {

    private final IMedecinDAO dao;

    public MedecinService(IMedecinDAO dao) {
        this.dao = dao;
    }

    public void ajouter(Medecin medecin) {
        valider(medecin);
        dao.save(medecin);
    }

    public void modifier(Medecin medecin) {
        valider(medecin);
        dao.update(medecin);
    }

    public void supprimer(Medecin medecin) {
        dao.delete(medecin);
    }

    public List<Medecin> lister() {
        return dao.getAll();
    }

    private void valider(Medecin m) {
        if (m.getNom() == null || m.getNom().isBlank())
            throw new IllegalArgumentException("Le nom est obligatoire.");
        if (m.getPrenom() == null || m.getPrenom().isBlank())
            throw new IllegalArgumentException("Le prénom est obligatoire.");
        if (m.getSpecialite() == null || m.getSpecialite().isBlank())
            throw new IllegalArgumentException("La spécialité est obligatoire.");
    }
}
