package lk.ijse.serenity.service.custom.impl;

import lk.ijse.serenity.dao.DAOFactory;
import lk.ijse.serenity.dao.custom.PaymentDAO;
import lk.ijse.serenity.dto.PaymentDTO;
import lk.ijse.serenity.entity.Payment;
import lk.ijse.serenity.entity.TherapySession;
import lk.ijse.serenity.service.custom.PaymentService;
import lk.ijse.serenity.util.SessionFactoryConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class PaymentServiceImpl implements PaymentService {

    private final PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PAYMENT);

    @Override
    public boolean savePayment(PaymentDTO dto) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            TherapySession ts = session.get(TherapySession.class, dto.getSessionId());

            if (ts == null) return false;

            Payment payment = new Payment();
            payment.setPaymentId(dto.getPaymentId());
            payment.setAmount(dto.getAmount());
            payment.setPaymentDate(dto.getDate());
            payment.setStatus(dto.getStatus());
            payment.setSession(ts);

            paymentDAO.save(payment, session);

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
    public boolean updatePayment(PaymentDTO dto) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            Payment payment = paymentDAO.get(dto.getPaymentId(), session);

            if (payment != null) {
                payment.setAmount(dto.getAmount());
                payment.setPaymentDate(dto.getDate());
                payment.setStatus(dto.getStatus());

                TherapySession ts = session.get(TherapySession.class, dto.getSessionId());
                if (ts != null) {
                    payment.setSession(ts);
                }

                paymentDAO.update(payment, session);
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
    public boolean deletePayment(String id) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            Payment payment = paymentDAO.get(id, session);
            if (payment != null) {
                paymentDAO.delete(payment, session);
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
    public List<PaymentDTO> getAllPayments() {
        Session session = SessionFactoryConfig.getInstance().getSession();
        try {
            List<Payment> list = paymentDAO.getAll(session);
            List<PaymentDTO> dtoList = new ArrayList<>();

            for (Payment p : list) {
                String patientName = p.getSession().getPatient().getName();
                String programName = p.getSession().getProgram().getProgramName();

                dtoList.add(new PaymentDTO(
                        p.getPaymentId(),
                        p.getSession().getSessionId(),
                        patientName,
                        programName,
                        p.getAmount(),
                        p.getPaymentDate(),
                        p.getStatus()
                ));
            }
            return dtoList;
        } finally {
            session.close();
        }
    }
}