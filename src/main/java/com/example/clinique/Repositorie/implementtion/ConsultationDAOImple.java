package com.example.clinique.Repositorie.implementtion;

import com.example.clinique.Repositorie.IConsultationDAO;
import com.example.clinique.config.FactoryJPA;
import com.example.clinique.model.Consultation;

import javax.persistence.EntityManager;
import java.util.List;

public class ConsultationDAOImple extends GenericDAOimple<Consultation> implements IConsultationDAO {

    public ConsultationDAOImple() {
        super(Consultation.class);
    }

    @Override
    public List<Consultation> findByPatientId(long patientId) {
        EntityManager em = FactoryJPA.getManager();
        List<Consultation> result = em.createQuery(
            "SELECT c FROM Consultation c WHERE c.patient.id = :pid ORDER BY c.dateConsultation DESC",
            Consultation.class)
            .setParameter("pid", patientId)
            .getResultList();
        em.close();
        return result;
    }
}
