package lk.ijse.serenity.dao.custom;

import lk.ijse.serenity.entity.User;
import org.hibernate.Session;

public interface UserDAO {
    boolean save(Object entity, Session session);

    boolean update(Object entity, Session session);

    boolean delete(Object entity, Session session);

    boolean save(User user, Session session);

    User findByUsername(String username, Session session);
}
