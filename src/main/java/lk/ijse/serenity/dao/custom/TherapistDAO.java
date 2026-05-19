package lk.ijse.serenity.dao.custom;

import lk.ijse.serenity.entity.Therapist;
import org.hibernate.Session;

import java.util.List;

public interface TherapistDAO {
    boolean save(Therapist therapist, Session session);

    List<Therapist> getAll(Session session);
}
