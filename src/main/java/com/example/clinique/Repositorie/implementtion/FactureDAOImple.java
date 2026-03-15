package com.example.clinique.Repositorie.implementtion;

import com.example.clinique.Repositorie.IFactureDAO;
import com.example.clinique.config.FactoryJPA;
import com.example.clinique.model.Facture;
import com.example.clinique.model.enums.StatutPaiement;

import javax.persistence.EntityManager;
import java.util.List;

public class FactureDAOImple extends GenericDAOimple<Facture> implements IFactureDAO {

    public FactureDAOImple() {
        super(Facture.class);
    }

    @Override
    public List<Facture> findByStatut(StatutPaiement statut) {
        EntityManager em = FactoryJPA.getManager();
        List<Facture> result = em.createQuery(
            "SELECT f FROM Facture f WHERE f.statut = :statut ORDER BY f.dateFacture DESC",
            Facture.class)
            .setParameter("statut", statut)
            .getResultList();
        em.close();
        return result;
    }
}
