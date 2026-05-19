package lk.ijse.serenity.dao;

import org.hibernate.Session;

import java.util.List;

public interface SuperDAO {
    boolean save(Object entity, Session session);
    boolean update(Object entity, Session session);
    boolean delete(Object entity, Session session);
}
