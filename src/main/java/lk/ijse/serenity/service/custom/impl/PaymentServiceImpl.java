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
            Payment p = new Payment(dto.getPaymentId(), dto.getAmount(), dto.getDate(), dto.getStatus(), ts);
            paymentDAO.save(p, session);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public List<PaymentDTO> getAll() {
        Session session = SessionFactoryConfig.getInstance().getSession();
        List<Payment> list = paymentDAO.getAll(session);
        List<PaymentDTO> dtos = new ArrayList<>();
        for (Payment p : list) {
            dtos.add(new PaymentDTO(p.getPaymentId(), p.getSession().getSessionId(),
                    p.getSession().getPatient().getName(), p.getSession().getProgram().getProgramName(),
                    p.getAmount(), p.getPaymentDate(), p.getStatus()));
        }
        session.close();
        return dtos;
    }
}