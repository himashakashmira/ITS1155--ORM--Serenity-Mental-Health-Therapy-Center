package lk.ijse.serenity.dao.custom.impl;

import lk.ijse.serenity.dao.custom.TherapistDAO;
import lk.ijse.serenity.entity.Therapist;
import org.hibernate.Session;
import java.util.List;

public class TherapistDAOImpl implements TherapistDAO {
    @Override
    public boolean save(Therapist therapist, Session session) {
        session.save(therapist);
        return true;
    }

    @Override
    public List<Therapist> getAll(Session session) {
        return session.createQuery("FROM Therapist").list();
    }
    // Update සහ Delete අවශ්‍ය පරිදි පසුව එකතු කරමු
}