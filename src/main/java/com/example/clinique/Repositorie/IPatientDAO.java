package com.example.clinique.Repositorie;

import com.example.clinique.model.Patient;

import java.util.List;

public interface IPatientDAO extends GenericDAO<Patient> {
    List<Patient> search(String keyword);
}
