package lk.ijse.serenity.dao.custom;

import lk.ijse.serenity.dao.SuperDAO;
import lk.ijse.serenity.entity.User;
import org.hibernate.Session;

public interface UserDAO extends SuperDAO {
    boolean save(User entity, Session session);

    boolean update(User entity, Session session);

    User get(String id, Session session);
}