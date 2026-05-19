package lk.ijse.serenity.dao.custom.impl;

import lk.ijse.serenity.dao.custom.UserDAO;
import lk.ijse.serenity.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean save(Object entity, Session session) {
        session.save(entity);
        return true;
    }

    @Override
    public boolean update(Object entity, Session session) {
        session.update(entity);
        return true;
    }

    @Override
    public boolean delete(Object entity, Session session) {
        session.delete(entity);
        return true;
    }

    @Override
    public boolean save(User user, Session session) {
        session.save(user);
        return true;
    }

    @Override
    public User findByUsername(String username, Session session) {
        Query<User> query = session.createQuery(
                "FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        return query.uniqueResult();
    }
}
