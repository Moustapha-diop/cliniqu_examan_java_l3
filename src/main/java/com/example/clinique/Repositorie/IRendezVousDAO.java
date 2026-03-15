package com.example.clinique.Repositorie;

import com.example.clinique.model.Medecin;
import com.example.clinique.model.RendezVous;

import java.time.LocalDate;
import java.util.List;

public interface IRendezVousDAO extends GenericDAO<RendezVous> {
    List<RendezVous> findByDate(LocalDate date);
    List<RendezVous> findByMedecin(Medecin medecin);
    boolean isMedecinOccupe(Medecin medecin, java.time.LocalDateTime dateHeure, Long excludeId);
}
