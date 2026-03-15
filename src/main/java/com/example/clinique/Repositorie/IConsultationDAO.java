package com.example.clinique.Repositorie;

import com.example.clinique.model.Consultation;

import java.util.List;

public interface IConsultationDAO extends GenericDAO<Consultation> {
    List<Consultation> findByPatientId(long patientId);
}
