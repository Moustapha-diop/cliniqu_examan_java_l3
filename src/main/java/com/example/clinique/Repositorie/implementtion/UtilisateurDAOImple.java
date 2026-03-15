package com.example.clinique.Repositorie.implementtion;

import com.example.clinique.Repositorie.IUtilisateurDAO;
import com.example.clinique.config.FactoryJPA;
import com.example.clinique.model.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

public class UtilisateurDAOImple extends GenericDAOimple<Utilisateur> implements IUtilisateurDAO {

    public UtilisateurDAOImple() {
        super(Utilisateur.class);
    }

    @Override
    public Optional<Utilisateur> findByUsername(String username) {
        EntityManager em = FactoryJPA.getManager();
        try {
            Utilisateur u = em.createQuery(
                "SELECT u FROM Utilisateur u WHERE u.username = :username", Utilisateur.class)
                .setParameter("username", username)
                .getSingleResult();
            return Optional.of(u);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}
