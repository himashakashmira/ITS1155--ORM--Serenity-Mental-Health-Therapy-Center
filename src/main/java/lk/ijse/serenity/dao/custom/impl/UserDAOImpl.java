package lk.ijse.serenity.dao.custom.impl;

import lk.ijse.serenity.dao.custom.UserDAO;
import lk.ijse.serenity.entity.User;
import org.hibernate.Session;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean save(User entity, Session session) {
        session.save(entity);
        return true;
    }

    @Override
    public boolean update(User entity, Session session) {
        session.update(entity);
        return true;
    }

    @Override
    public User get(String id, Session session) {
        return session.get(User.class, id);
    }
}