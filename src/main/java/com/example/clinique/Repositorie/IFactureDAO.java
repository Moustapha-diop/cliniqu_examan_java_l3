package com.example.clinique.Repositorie;

import com.example.clinique.model.Facture;
import com.example.clinique.model.enums.StatutPaiement;

import java.util.List;

public interface IFactureDAO extends GenericDAO<Facture> {
    List<Facture> findByStatut(StatutPaiement statut);
}
