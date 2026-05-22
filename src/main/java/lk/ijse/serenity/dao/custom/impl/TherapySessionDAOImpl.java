package lk.ijse.serenity.dao.custom.impl;

import lk.ijse.serenity.dao.custom.TherapySessionDAO;
import lk.ijse.serenity.entity.TherapySession;
import org.hibernate.Session;

import java.util.List;

public class TherapySessionDAOImpl implements TherapySessionDAO {
    @Override
    public boolean save(TherapySession entity, Session session) {
        session.save(entity);
        return true;
    }

    @Override
    public boolean update(TherapySession entity, Session session) {
        session.update(entity);
        return true;
    }

    @Override
    public boolean delete(TherapySession entity, Session session) {
        session.delete(entity);
        return true;
    }

    @Override
    public TherapySession get(int id, Session session) {
        return session.get(TherapySession.class, id);
    }

    @Override
    public List<TherapySession> getAll(Session session) {
        return session.createQuery("FROM TherapySession", TherapySession.class).list();
    }
}