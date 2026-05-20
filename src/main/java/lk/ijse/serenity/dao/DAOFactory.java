package lk.ijse.serenity.dao;

import lk.ijse.serenity.dao.custom.impl.PatientDAOImpl;
import lk.ijse.serenity.dao.custom.impl.PaymentDAOImpl;
import lk.ijse.serenity.dao.custom.impl.TherapistDAOImpl;
import lk.ijse.serenity.dao.custom.impl.UserDAOImpl;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return daoFactory == null ? daoFactory = new DAOFactory() : daoFactory;
    }

    public Object getDAO(DAOType daoType) {
        switch (daoType) {
            case PATIENT:
                return new PatientDAOImpl();
            case THERAPIST:
                return new TherapistDAOImpl();
            case USER:
                return new UserDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            default:
                return null;
        }
    }

    public enum DAOType {
        PATIENT, THERAPIST, PROGRAM, PAYMENT, USER
    }
}