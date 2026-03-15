package com.example.clinique.Repositorie;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T> {
    void save(T entite);
    void update(T entite);
    void delete(T entite);
    List<T> getAll();
    Optional<T> findById(long id);
}
