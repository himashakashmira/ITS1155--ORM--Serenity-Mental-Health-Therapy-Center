package lk.ijse.serenity.dao.custom;

import lk.ijse.serenity.dao.SuperDAO;
import lk.ijse.serenity.entity.Patient;
import org.hibernate.Session;

import java.util.List;

public interface PatientDAO extends SuperDAO {
    boolean save(Patient patient, Session session);

    boolean update(Patient patient, Session session);

    boolean delete(Patient patient, Session session);

    Patient get(String id, Session session);

    List<Patient> getAll(Session session);
}