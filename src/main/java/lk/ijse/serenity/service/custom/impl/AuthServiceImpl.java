package lk.ijse.serenity.service.custom.impl;

import lk.ijse.serenity.dao.DAOFactory;
import lk.ijse.serenity.dao.custom.UserDAO;
import lk.ijse.serenity.dto.UserDTO;
import lk.ijse.serenity.entity.User;
import lk.ijse.serenity.exception.ServiceException;
import lk.ijse.serenity.service.custom.AuthService;
import lk.ijse.serenity.util.PasswordUtil;
import lk.ijse.serenity.util.SessionFactoryConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AuthServiceImpl implements AuthService {

    private final UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.USER);

    @Override
    public UserDTO login(String username, String plainPassword) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        try {
            User user = userDAO.findByUsername(username, session);
            if (user == null) return null;
            if (!PasswordUtil.checkPassword(plainPassword, user.getPassword())) return null;
            return new UserDTO(user.getUsername(), null, user.getRole());
        } finally {
            session.close();
        }
    }

    @Override
    public boolean createUser(UserDTO userDTO) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            String hashed = PasswordUtil.hashPassword(userDTO.getPassword());
            User user = new User(userDTO.getUsername(), hashed, userDTO.getRole());
            userDAO.save(user, session);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            throw new ServiceException("Failed to create user.", e);
        } finally {
            session.close();
        }
    }
}
