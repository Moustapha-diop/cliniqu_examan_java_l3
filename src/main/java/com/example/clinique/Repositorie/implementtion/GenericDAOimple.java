package com.example.clinique.Repositorie.implementtion;

import com.example.clinique.Repositorie.GenericDAO;
import com.example.clinique.config.FactoryJPA;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class GenericDAOimple<T> implements GenericDAO<T> {

    private Class<T> type;

    public GenericDAOimple(Class<T> type) {
        this.type = type;
    }

    @Override
    public void save(T entite) {
        EntityManager em = FactoryJPA.getManager();
        em.getTransaction().begin();
        em.persist(entite);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void update(T entite) {
        EntityManager em = FactoryJPA.getManager();
        em.getTransaction().begin();
        em.merge(entite);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(T entite) {
        EntityManager em = FactoryJPA.getManager();
        em.getTransaction().begin();
        em.remove(em.merge(entite));
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<T> getAll() {
        EntityManager em = FactoryJPA.getManager();
        List<T> data = em.createQuery("from " + type.getSimpleName(), type).getResultList();
        em.close();
        return data;
    }

    @Override
    public Optional<T> findById(long id) {
        EntityManager em = FactoryJPA.getManager();
        T result = em.find(type, id);
        em.close();
        return Optional.ofNullable(result);
    }
}
