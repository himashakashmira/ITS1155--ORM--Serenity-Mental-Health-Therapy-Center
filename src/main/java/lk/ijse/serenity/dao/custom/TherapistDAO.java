package lk.ijse.serenity.dao.custom;

import lk.ijse.serenity.dao.SuperDAO;
import lk.ijse.serenity.entity.Therapist;
import org.hibernate.Session;

import java.util.List;

public interface TherapistDAO extends SuperDAO {
    boolean save(Therapist therapist, Session session);

    boolean update(Therapist therapist, Session session);

    boolean delete(Therapist therapist, Session session);

    Therapist get(String id, Session session);

    List<Therapist> getAll(Session session);
}