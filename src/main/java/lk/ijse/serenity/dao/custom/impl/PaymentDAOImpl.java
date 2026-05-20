package lk.ijse.serenity.dao.custom.impl;

import lk.ijse.serenity.dao.custom.PaymentDAO;
import lk.ijse.serenity.entity.Payment;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public boolean save(Payment entity, Session session) {
        session.save(entity);
        return true;
    }

    @Override
    public boolean update(Payment entity, Session session) {
        session.update(entity);
        return true;
    }

    @Override
    public boolean delete(Payment entity, Session session) {
        session.delete(entity);
        return true;
    }

    @Override
    public Payment get(String id, Session session) {
        return session.get(Payment.class, id);
    }

    @Override
    public List<Payment> getAll(Session session) {
        String hql = "FROM Payment p JOIN FETCH p.session s JOIN FETCH s.patient JOIN FETCH s.program";
        Query<Payment> query = session.createQuery(hql, Payment.class);
        return query.list();
    }
}