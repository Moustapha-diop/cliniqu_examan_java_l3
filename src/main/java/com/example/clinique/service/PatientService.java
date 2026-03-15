package com.example.clinique.service;

import com.example.clinique.Repositorie.IPatientDAO;
import com.example.clinique.model.Patient;

import java.util.List;

public class PatientService {

    private final IPatientDAO dao;

    public PatientService(IPatientDAO dao) {
        this.dao = dao;
    }

    public void ajouter(Patient patient) {
        valider(patient);
        dao.save(patient);
    }

    public void modifier(Patient patient) {
        valider(patient);
        dao.update(patient);
    }

    public void supprimer(Patient patient) {
        dao.delete(patient);
    }

    public List<Patient> lister() {
        return dao.getAll();
    }

    public List<Patient> rechercher(String keyword) {
        if (keyword == null || keyword.isBlank()) return lister();
        return dao.search(keyword.trim());
    }

    private void valider(Patient patient) {
        if (patient.getNom() == null || patient.getNom().isBlank())
            throw new IllegalArgumentException("Le nom est obligatoire.");
        if (patient.getPrenom() == null || patient.getPrenom().isBlank())
            throw new IllegalArgumentException("Le prénom est obligatoire.");
        if (patient.getDateNaissance() == null)
            throw new IllegalArgumentException("La date de naissance est obligatoire.");
        if (patient.getTelephone() == null || patient.getTelephone().isBlank())
            throw new IllegalArgumentException("Le téléphone est obligatoire.");
        if (patient.getSexe() == null)
            throw new IllegalArgumentException("Le sexe est obligatoire.");
    }
}
