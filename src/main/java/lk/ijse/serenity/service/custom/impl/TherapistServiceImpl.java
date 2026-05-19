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
        Transaction transaction = session.beginTransaction();
        try {
            Therapist therapist = new Therapist();
            therapist.setTherapistId(dto.getTherapistId());
            therapist.setName(dto.getName());
            therapist.setSpecialization(dto.getSpecialization());
            therapist.setPhone(dto.getPhone());

            therapistDAO.save(therapist, session);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public List<TherapistDTO> getAllTherapists() {
        Session session = SessionFactoryConfig.getInstance().getSession();
        try {
            List<Therapist> all = therapistDAO.getAll(session);
            List<TherapistDTO> dtos = new ArrayList<>();
            for (Therapist t : all) {
                dtos.add(new TherapistDTO(t.getTherapistId(), t.getName(), t.getSpecialization(), t.getPhone()));
            }
            return dtos;
        } finally {
            session.close();
        }
    }
}