package lk.ijse.serenity.service.custom.impl;

import lk.ijse.serenity.dao.DAOFactory;
import lk.ijse.serenity.dao.custom.TherapyProgramDAO;
import lk.ijse.serenity.dto.TherapyProgramDTO;
import lk.ijse.serenity.entity.TherapyProgram;
import lk.ijse.serenity.service.custom.TherapyProgramService;
import lk.ijse.serenity.util.SessionFactoryConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TherapyProgramServiceImpl implements TherapyProgramService {

    private final TherapyProgramDAO programDAO = (TherapyProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PROGRAM);

    @Override
    public boolean saveProgram(TherapyProgramDTO dto) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            TherapyProgram program = new TherapyProgram();
            program.setProgramId(dto.getProgramId());
            program.setProgramName(dto.getProgramName());
            program.setDuration(dto.getDuration());
            program.setFee(dto.getFee());

            programDAO.save(program, session);
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
    public boolean updateProgram(TherapyProgramDTO dto) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            TherapyProgram program = programDAO.get(dto.getProgramId(), session);
            if (program != null) {
                program.setProgramName(dto.getProgramName());
                program.setDuration(dto.getDuration());
                program.setFee(dto.getFee());

                programDAO.update(program, session);
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
    public boolean deleteProgram(String id) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            TherapyProgram program = programDAO.get(id, session);
            if (program != null) {
                programDAO.delete(program, session);
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
    public List<TherapyProgramDTO> getAllPrograms() {
        Session session = SessionFactoryConfig.getInstance().getSession();
        try {
            List<TherapyProgram> list = programDAO.getAll(session);
            List<TherapyProgramDTO> dtoList = new ArrayList<>();
            for (TherapyProgram p : list) {
                dtoList.add(new TherapyProgramDTO(p.getProgramId(), p.getProgramName(), p.getDuration(), p.getFee()));
            }
            return dtoList;
        } finally {
            session.close();
        }
    }
}