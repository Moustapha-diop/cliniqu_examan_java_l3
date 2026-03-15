package com.example.clinique.service;

import com.example.clinique.Repositorie.IFactureDAO;
import com.example.clinique.model.Consultation;
import com.example.clinique.model.Facture;
import com.example.clinique.model.enums.StatutPaiement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class FactureService {

    private final IFactureDAO dao;

    public FactureService(IFactureDAO dao) {
        this.dao = dao;
    }

    public void generer(Consultation consultation, double montant, com.example.clinique.model.enums.ModePaiement modePaiement) {
        if (consultation == null) throw new IllegalArgumentException("La consultation est obligatoire.");
        if (montant <= 0) throw new IllegalArgumentException("Le montant doit être positif.");
        if (modePaiement == null)
            throw new IllegalArgumentException("Le mode de paiement est obligatoire.");

        Facture facture = new Facture();
        facture.setConsultation(consultation);
        facture.setDateFacture(LocalDateTime.now());
        facture.setMontantTotal(montant);
        facture.setModePaiement(modePaiement);
        facture.setStatut(StatutPaiement.NON_PAYE);
        dao.save(facture);
    }

    public void marquerPayee(Facture facture) {
        facture.setStatut(StatutPaiement.PAYE);
        dao.update(facture);
    }

    public List<Facture> lister() {
        return dao.getAll();
    }

    public List<Facture> listerParStatut(StatutPaiement statut) {
        return dao.findByStatut(statut);
    }
}
