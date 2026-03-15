package com.example.clinique.Repositorie.implementtion;

import com.example.clinique.Repositorie.IRendezVousDAO;
import com.example.clinique.config.FactoryJPA;
import com.example.clinique.model.Medecin;
import com.example.clinique.model.RendezVous;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RendezVousDAOImple extends GenericDAOimple<RendezVous> implements IRendezVousDAO {

    public RendezVousDAOImple() {
        super(RendezVous.class);
    }

    @Override
    public List<RendezVous> findByDate(LocalDate date) {
        EntityManager em = FactoryJPA.getManager();
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        List<RendezVous> result = em.createQuery(
            "SELECT rv FROM RendezVous rv WHERE rv.dateHeure BETWEEN :start AND :end ORDER BY rv.dateHeure",
            RendezVous.class)
            .setParameter("start", start)
            .setParameter("end", end)
            .getResultList();
        em.close();
        return result;
    }

    @Override
    public List<RendezVous> findByMedecin(Medecin medecin) {
        EntityManager em = FactoryJPA.getManager();
        List<RendezVous> result = em.createQuery(
            "SELECT rv FROM RendezVous rv WHERE rv.medecin = :medecin ORDER BY rv.dateHeure DESC",
            RendezVous.class)
            .setParameter("medecin", medecin)
            .getResultList();
        em.close();
        return result;
    }

    @Override
    public boolean isMedecinOccupe(Medecin medecin, LocalDateTime dateHeure, Long excludeId) {
        EntityManager em = FactoryJPA.getManager();
        try {
            LocalDateTime start = dateHeure.minusMinutes(29);
            LocalDateTime end = dateHeure.plusMinutes(29);
            String jpql = "SELECT COUNT(rv) FROM RendezVous rv WHERE rv.medecin = :medecin " +
                          "AND rv.dateHeure BETWEEN :start AND :end AND rv.statut != 'ANNULE'";
            if (excludeId != null) jpql += " AND rv.id != :excludeId";

            var query = em.createQuery(jpql, Long.class)
                .setParameter("medecin", medecin)
                .setParameter("start", start)
                .setParameter("end", end);
            if (excludeId != null) query.setParameter("excludeId", excludeId);

            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
}
