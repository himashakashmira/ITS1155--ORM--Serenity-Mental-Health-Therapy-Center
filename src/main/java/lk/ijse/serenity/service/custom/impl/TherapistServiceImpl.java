package lk.ijse.serenity.service.custom.impl;

import lk.ijse.serenity.dao.DAOFactory;
import lk.ijse.serenity.dao.custom.TherapistDAO;
import lk.ijse.serenity.dto.TherapistDTO;
import lk.ijse.serenity.entity.Therapist;
import lk.ijse.serenity.service.custom.TherapistService;
import lk.ijse.serenity.util.SessionFactoryConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TherapistServiceImpl implements TherapistService {

    private final TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);

    @Override
    public boolean saveTherapist(TherapistDTO dto) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            therapistDAO.save(new Therapist(
                    dto.getTherapistId(),
                    dto.getName(),
                    dto.getSpecialization(),
                    dto.getPhone(),
                    dto.getEmail()
            ), session);
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
    public boolean updateTherapist(TherapistDTO dto) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            Therapist t = therapistDAO.get(dto.getTherapistId(), session);
            if (t != null) {
                t.setName(dto.getName());
                t.setSpecialization(dto.getSpecialization());
                t.setPhone(dto.getPhone());
                therapistDAO.update(t, session);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            tx.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteTherapist(String id) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            Therapist t = therapistDAO.get(id, session);
            if (t != null) {
                therapistDAO.delete(t, session);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            tx.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapistDTO> getAllTherapists() {
        Session session = SessionFactoryConfig.getInstance().getSession();
        try {
            List<Therapist> list = therapistDAO.getAll(session);
            List<TherapistDTO> dtoList = new ArrayList<>();
            for (Therapist t : list) {
                dtoList.add(new TherapistDTO(
                        t.getTherapistId(),
                        t.getName(),
                        t.getSpecialization(),
                        t.getPhone(),
                        t.getEmail()
                ));
            }
            return dtoList;
        } finally {
            session.close();
        }
    }

    @Override
    public String getNextTherapistId() {
        Session session = SessionFactoryConfig.getInstance().getSession();
        String hql = "SELECT t.therapistId FROM Therapist t ORDER BY t.therapistId DESC";
        String lastId = (String) session.createQuery(hql).setMaxResults(1).uniqueResult();
        session.close();

        if (lastId == null) return "T001";
        int idNum = Integer.parseInt(lastId.substring(1));
        return "T" + String.format("%03d", idNum + 1);
    }
}