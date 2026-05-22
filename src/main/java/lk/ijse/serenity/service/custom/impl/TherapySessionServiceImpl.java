package lk.ijse.serenity.service.custom.impl;

import lk.ijse.serenity.dao.DAOFactory;
import lk.ijse.serenity.dao.custom.TherapySessionDAO;
import lk.ijse.serenity.dto.TherapySessionDTO;
import lk.ijse.serenity.entity.*;
import lk.ijse.serenity.exception.SchedulingConflictException;
import lk.ijse.serenity.service.custom.TherapySessionService;
import lk.ijse.serenity.util.SessionFactoryConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TherapySessionServiceImpl implements TherapySessionService {

    private final TherapySessionDAO sessionDAO =
            (TherapySessionDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.SESSION);

    @Override
    public boolean bookSession(TherapySessionDTO dto) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            // Check scheduling conflict
            Long conflictCount = (Long) session.createQuery(
                            "SELECT COUNT(s) FROM TherapySession s " +
                                    "WHERE s.patient.patientId = :pid " +
                                    "AND s.program.programId = :progId " +
                                    "AND s.sessionDate = :date " +
                                    "AND s.status = 'Scheduled'")
                    .setParameter("pid", dto.getPatientId())
                    .setParameter("progId", dto.getProgramId())
                    .setParameter("date", dto.getSessionDate())
                    .uniqueResult();

            if (conflictCount != null && conflictCount > 0) {
                throw new SchedulingConflictException(
                        "This patient already has a scheduled session for '" +
                                dto.getProgramId() + "' on " + dto.getSessionDate() + "!");
            }

            TherapySession ts = new TherapySession();
            ts.setSessionDate(dto.getSessionDate());
            ts.setStatus(dto.getStatus() != null ? dto.getStatus() : "Scheduled");
            ts.setPatient(session.get(Patient.class, dto.getPatientId()));
            ts.setTherapist(session.get(Therapist.class, extractTherapistId(dto.getTherapistId())));
            ts.setProgram(session.get(TherapyProgram.class, dto.getProgramId()));

            if (ts.getPatient() == null || ts.getTherapist() == null || ts.getProgram() == null) {
                throw new SchedulingConflictException("Invalid patient, therapist or program selected.");
            }

            sessionDAO.save(ts, session);
            tx.commit();
            return true;
        } catch (SchedulingConflictException e) {
            if (tx != null) tx.rollback();
            throw e;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    private String extractTherapistId(String comboValue) {
        if (comboValue == null) return null;
        return comboValue.contains(" - ") ? comboValue.split(" - ")[0].trim() : comboValue.trim();
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
                ts.setTherapist(session.get(Therapist.class, extractTherapistId(dto.getTherapistId())));
                ts.setProgram(session.get(TherapyProgram.class, dto.getProgramId()));
                sessionDAO.update(ts, session);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
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
                String pId = s.getPatient() != null ? s.getPatient().getPatientId() : "N/A";
                String pName = s.getPatient() != null ? s.getPatient().getName() : "Unknown";
                String tId = s.getTherapist() != null ? s.getTherapist().getTherapistId() : "N/A";
                String tName = s.getTherapist() != null ? s.getTherapist().getName() : "Unknown";
                String prId = s.getProgram() != null ? s.getProgram().getProgramId() : "N/A";
                String prName = s.getProgram() != null ? s.getProgram().getProgramName() : "Unknown";

                dtos.add(new TherapySessionDTO(
                        s.getSessionId(), s.getSessionDate(), s.getStatus(),
                        pId, tId, prId, pName, prName, tName));
            }
            return dtos;
        } finally {
            session.close();
        }
    }
}