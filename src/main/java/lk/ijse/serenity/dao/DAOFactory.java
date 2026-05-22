package lk.ijse.serenity.dao;

import lk.ijse.serenity.dao.custom.impl.*;

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
            case PROGRAM:
                return new TherapyProgramDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            case SESSION:
                return new TherapySessionDAOImpl();
            default:
                return null;
        }
    }

    public enum DAOType {
        PATIENT, THERAPIST, PROGRAM, PAYMENT, SESSION, USER
    }
}