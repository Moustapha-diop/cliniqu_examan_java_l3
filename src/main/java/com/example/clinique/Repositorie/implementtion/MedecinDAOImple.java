package com.example.clinique.Repositorie.implementtion;

import com.example.clinique.Repositorie.IMedecinDAO;
import com.example.clinique.model.Medecin;

public class MedecinDAOImple extends GenericDAOimple<Medecin> implements IMedecinDAO {
    public MedecinDAOImple() {
        super(Medecin.class);
    }
}
