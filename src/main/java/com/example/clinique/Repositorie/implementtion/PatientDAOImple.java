package com.example.clinique.Repositorie.implementtion;

import com.example.clinique.Repositorie.IPatientDAO;
import com.example.clinique.config.FactoryJPA;
import com.example.clinique.model.Patient;

import javax.persistence.EntityManager;
import java.util.List;

public class PatientDAOImple extends GenericDAOimple<Patient> implements IPatientDAO {

    public PatientDAOImple() {
        super(Patient.class);
    }

    @Override
    public List<Patient> search(String keyword) {
        EntityManager em = FactoryJPA.getManager();
        String q = "%" + keyword.toLowerCase() + "%";
        List<Patient> result = em.createQuery(
            "SELECT p FROM Patient p WHERE LOWER(p.nom) LIKE :q OR LOWER(p.prenom) LIKE :q OR p.telephone LIKE :q",
            Patient.class)
            .setParameter("q", q)
            .getResultList();
        em.close();
        return result;
    }
}
