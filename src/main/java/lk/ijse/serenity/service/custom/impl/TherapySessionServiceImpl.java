package lk.ijse.serenity.service.custom.impl;

import lk.ijse.serenity.dao.DAOFactory;
import lk.ijse.serenity.dao.custom.TherapySessionDAO;
import lk.ijse.serenity.dto.TherapySessionDTO;
import lk.ijse.serenity.entity.*;
import lk.ijse.serenity.service.custom.TherapySessionService;
import lk.ijse.serenity.util.SessionFactoryConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TherapySessionServiceImpl implements TherapySessionService {

    private final TherapySessionDAO sessionDAO = (TherapySessionDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.SESSION);

    @Override
    public boolean bookSession(TherapySessionDTO dto) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            TherapySession ts = new TherapySession();
            ts.setSessionDate(dto.getSessionDate());
            ts.setStatus(dto.getStatus());

            // Get the Objects fROM Database
            ts.setPatient(session.get(Patient.class, dto.getPatientId()));
            ts.setTherapist(session.get(Therapist.class, dto.getTherapistId()));
            ts.setProgram(session.get(TherapyProgram.class, dto.getProgramId()));

            sessionDAO.save(ts, session);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean updateSession(TherapySessionDTO dto) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            TherapySession ts = sessionDAO.get(dto.getSessionId(), session);
            if (ts != null) {
                ts.setSessionDate(dto.getSessionDate());
                ts.setStatus(dto.getStatus());
                ts.setTherapist(session.get(Therapist.class, dto.getTherapistId()));
                ts.setProgram(session.get(TherapyProgram.class, dto.getProgramId()));

                sessionDAO.update(ts, session);
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

    @Override
    public boolean deleteSession(int id) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            TherapySession ts = sessionDAO.get(id, session);
            if (ts != null) {
                sessionDAO.delete(ts, session);
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

    @Override
    public List<TherapySessionDTO> getAllSessions() {
        Session session = SessionFactoryConfig.getInstance().getSession();
        try {
            List<TherapySession> list = sessionDAO.getAll(session);
            List<TherapySessionDTO> dtos = new ArrayList<>();
            for (TherapySession s : list) {
                // check null
                String pId = (s.getPatient() != null) ? s.getPatient().getPatientId() : "N/A";
                String pName = (s.getPatient() != null) ? s.getPatient().getName() : "Unknown";

                String tId = (s.getTherapist() != null) ? s.getTherapist().getTherapistId() : "N/A";
                String tName = (s.getTherapist() != null) ? s.getTherapist().getName() : "Unknown";

                String prId = (s.getProgram() != null) ? s.getProgram().getProgramId() : "N/A";
                String prName = (s.getProgram() != null) ? s.getProgram().getProgramName() : "Unknown";

                dtos.add(new TherapySessionDTO(
                        s.getSessionId(),
                        s.getSessionDate(),
                        s.getStatus(),
                        pId, tId, prId,
                        pName, prName, tName
                ));
            }
            return dtos;
        } finally {
            session.close();
        }
    }
}