package lk.ijse.serenity.util;

import lk.ijse.serenity.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.Properties;

public class SessionFactoryConfig {
    private static SessionFactoryConfig factoryConfig;
    private final SessionFactory sessionFactory;

    private SessionFactoryConfig() {
        Configuration configuration = new Configuration();
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("hibernate.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        configuration.setProperties(properties);

        // Entity classes connecter
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Patient.class);
        configuration.addAnnotatedClass(Therapist.class);
        configuration.addAnnotatedClass(TherapyProgram.class);
        configuration.addAnnotatedClass(TherapySession.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    public static SessionFactoryConfig getInstance() {
        return (factoryConfig == null) ? factoryConfig = new SessionFactoryConfig() : factoryConfig;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}