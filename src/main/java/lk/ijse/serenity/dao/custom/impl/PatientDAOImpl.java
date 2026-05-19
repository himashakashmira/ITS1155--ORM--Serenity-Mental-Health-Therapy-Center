package lk.ijse.serenity.dao.custom.impl;

import lk.ijse.serenity.dao.custom.PatientDAO;
import lk.ijse.serenity.entity.Patient;
import org.hibernate.Session;

import java.util.List;

public class PatientDAOImpl implements PatientDAO {
    @Override
    public boolean save(Patient patient, Session session) {
        session.save(patient);
        return true;
    }

    @Override
    public Patient get(String id, Session session) {
        return session.get(Patient.class, id);
    }

    @Override
    public boolean update(Patient patient, Session session) {
        session.update(patient);
        return true;
    }

    @Override
    public boolean delete(Patient patient, Session session) {
        session.delete(patient);
        return true;
    }

    @Override
    public List<Patient> getAll(Session session) {
        return session.createQuery("FROM Patient").list();
    }
}