package lk.ijse.serenity.dao.custom;

import lk.ijse.serenity.dao.SuperDAO;
import lk.ijse.serenity.entity.TherapyProgram;
import org.hibernate.Session;

import java.util.List;

public interface TherapyProgramDAO extends SuperDAO {
    boolean save(TherapyProgram entity, Session session);

    boolean update(TherapyProgram entity, Session session);

    boolean delete(TherapyProgram entity, Session session);

    TherapyProgram get(String id, Session session);

    List<TherapyProgram> getAll(Session session);
}