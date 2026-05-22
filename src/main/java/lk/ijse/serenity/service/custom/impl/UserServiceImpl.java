package lk.ijse.serenity.service.custom.impl;

import lk.ijse.serenity.dao.DAOFactory;
import lk.ijse.serenity.dao.custom.UserDAO;
import lk.ijse.serenity.dto.UserDTO;
import lk.ijse.serenity.entity.User;
import lk.ijse.serenity.service.custom.UserService;
import lk.ijse.serenity.util.PasswordUtil;
import lk.ijse.serenity.util.SessionFactoryConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserServiceImpl implements UserService {

    private final UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.USER);

    @Override
    public UserDTO searchUser(String username) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        try {
            User user = userDAO.get(username, session);
            if (user != null) {
                return new UserDTO(user.getUsername(), user.getPassword(), user.getRole());
            }
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public boolean saveUser(UserDTO userDTO) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            String hashed = PasswordUtil.hashPassword(userDTO.getPassword());
            userDAO.save(new User(userDTO.getUsername(), hashed, userDTO.getRole()), session);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            User user = userDAO.get(userDTO.getUsername(), session);
            if (user != null) {
                user.setPassword(PasswordUtil.hashPassword(userDTO.getPassword()));
                userDAO.update(user, session);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        } finally {
            session.close();
        }
    }
}