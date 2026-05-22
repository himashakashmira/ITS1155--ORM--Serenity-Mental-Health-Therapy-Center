package lk.ijse.serenity.dao.custom.impl;

import lk.ijse.serenity.dao.custom.TherapyProgramDAO;
import lk.ijse.serenity.entity.TherapyProgram;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class TherapyProgramDAOImpl implements TherapyProgramDAO {

    @Override
    public boolean save(TherapyProgram entity, Session session) {
        session.save(entity);
        return true;
    }

    @Override
    public boolean update(TherapyProgram entity, Session session) {
        session.update(entity);
        return true;
    }

    @Override
    public boolean delete(TherapyProgram entity, Session session) {
        session.delete(entity);
        return true;
    }

    @Override
    public TherapyProgram get(String id, Session session) {
        return session.get(TherapyProgram.class, id);
    }

    @Override
    public List<TherapyProgram> getAll(Session session) {
        Query<TherapyProgram> query = session.createQuery("FROM TherapyProgram", TherapyProgram.class);
        return query.list();
    }
}