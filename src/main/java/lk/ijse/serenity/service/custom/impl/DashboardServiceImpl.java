package lk.ijse.serenity.service.custom.impl;

import lk.ijse.serenity.service.custom.DashboardService;
import lk.ijse.serenity.util.SessionFactoryConfig;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class DashboardServiceImpl implements DashboardService {

    @Override
    public long getPatientCount() {
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            Long count = (Long) session.createQuery("SELECT COUNT(p) FROM Patient p").uniqueResult();
            return count != null ? count : 0L;
        }
    }

    @Override
    public double getTotalRevenue() {
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            Double sum = (Double) session.createQuery(
                    "SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'Paid'").uniqueResult();
            return sum != null ? sum : 0.0;
        }
    }

    // patients enrolled in ALL available therapy programs
    @Override
    public List<String> getSpecialPatients() {
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            String hql = "SELECT p.name FROM Patient p WHERE " +
                    "(SELECT COUNT(DISTINCT s.program.programId) FROM TherapySession s " +
                    " WHERE s.patient.patientId = p.patientId) = " +
                    "(SELECT COUNT(pr) FROM TherapyProgram pr)";
            return session.createQuery(hql, String.class).list();
        }
    }

    // Today's sessions count
    @Override
    public long getTodaysSessionCount() {
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            Long count = (Long) session.createQuery(
                            "SELECT COUNT(s) FROM TherapySession s WHERE s.sessionDate = :today")
                    .setParameter("today", LocalDate.now())
                    .uniqueResult();
            return count != null ? count : 0L;
        }
    }

    // Active therapists
    @Override
    public long getActiveTherapistCount() {
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            Long count = (Long) session.createQuery(
                            "SELECT COUNT(DISTINCT s.therapist.therapistId) FROM TherapySession s " +
                                    "WHERE s.status = 'Scheduled' OR s.status = 'Completed'")
                    .uniqueResult();
            return count != null ? count : 0L;
        }
    }

    // Pending payments count
    @Override
    public long getPendingPaymentCount() {
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            Long count = (Long) session.createQuery(
                            "SELECT COUNT(p) FROM Payment p WHERE p.status = 'Pending'")
                    .uniqueResult();
            return count != null ? count : 0L;
        }
    }
}