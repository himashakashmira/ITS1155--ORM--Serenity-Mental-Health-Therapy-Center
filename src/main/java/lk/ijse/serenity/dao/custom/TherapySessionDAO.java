package lk.ijse.serenity.dao.custom;

import lk.ijse.serenity.dao.SuperDAO;
import lk.ijse.serenity.entity.TherapySession;
import org.hibernate.Session;

import java.util.List;

public interface TherapySessionDAO extends SuperDAO {
    boolean save(TherapySession entity, Session session);

    boolean update(TherapySession entity, Session session);

    boolean delete(TherapySession entity, Session session);

    TherapySession get(int id, Session session);

    List<TherapySession> getAll(Session session);
}