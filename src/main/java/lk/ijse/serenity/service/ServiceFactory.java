package lk.ijse.serenity.service;

import lk.ijse.serenity.service.custom.impl.*;

import static lk.ijse.serenity.dao.DAOFactory.DAOType.PROGRAM;

public class ServiceFactory {
    private static ServiceFactory serviceFactory;

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return serviceFactory == null ? serviceFactory = new ServiceFactory() : serviceFactory;
    }

    public enum ServiceType {
        PATIENT, THERAPIST, PAYMENT, PROGRAM, SESSION, AUTH
    }

    public <T> T getService(ServiceType type) {
        switch (type) {
            case PATIENT:
                return (T) new PatientServiceImpl();
            case THERAPIST:
                return (T) new TherapistServiceImpl();
            case PROGRAM:
                return (T) new TherapyProgramServiceImpl();
            case SESSION:
                return (T) new TherapySessionServiceImpl();
            case PAYMENT:
                return (T) new PaymentServiceImpl();
            case AUTH:
                return (T) new AuthServiceImpl();
            default:
                throw new IllegalArgumentException("Unknown ServiceType: " + type);
        }
    }
}