package lk.ijse.serenity.dao.custom;

import lk.ijse.serenity.dao.SuperDAO;
import lk.ijse.serenity.entity.Payment;
import org.hibernate.Session;

import java.util.List;

public interface PaymentDAO extends SuperDAO {
    boolean save(Payment entity, Session session);

    boolean update(Payment entity, Session session);

    boolean delete(Payment entity, Session session);

    Payment get(String id, Session session);

    List<Payment> getAll(Session session);
}