package lk.ijse.serenity.service.custom.impl;

import lk.ijse.serenity.dao.DAOFactory;
import lk.ijse.serenity.dao.custom.PatientDAO;
import lk.ijse.serenity.dto.PatientDTO;
import lk.ijse.serenity.entity.Patient;
import lk.ijse.serenity.exception.RegistrationException;
import lk.ijse.serenity.service.custom.PatientService;
import lk.ijse.serenity.util.SessionFactoryConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;

public class PatientServiceImpl implements PatientService {

    private final PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);

    @Override
    public boolean savePatient(PatientDTO dto) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            // Check duplicate
            Patient existing = patientDAO.get(dto.getPatientId(), session);
            if (existing != null) {
                throw new RegistrationException("Patient ID '" + dto.getPatientId() + "' already exists!");
            }
            Patient patient = new Patient(
                    dto.getPatientId(), dto.getName(), dto.getAddress(),
                    dto.getEmail(), dto.getPhone(), dto.getRegDate()
            );
            patientDAO.save(patient, session);
            transaction.commit();
            return true;
        } catch (RegistrationException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } catch (ConstraintViolationException e) {
            if (transaction != null) transaction.rollback();
            throw new RegistrationException("Duplicate entry – email or ID already registered.", e);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean updatePatient(PatientDTO dto) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Patient patient = patientDAO.get(dto.getPatientId(), session);
            if (patient != null) {
                patient.setName(dto.getName());
                patient.setAddress(dto.getAddress());
                patient.setEmail(dto.getEmail());
                patient.setPhone(dto.getPhone());
                patientDAO.update(patient, session);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deletePatient(String id) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Patient patient = patientDAO.get(id, session);
            if (patient != null) {
                patientDAO.delete(patient, session);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        Session session = SessionFactoryConfig.getInstance().getSession();
        try {
            List<Patient> list = patientDAO.getAll(session);
            List<PatientDTO> dtoList = new ArrayList<>();
            for (Patient p : list) {
                dtoList.add(new PatientDTO(p.getPatientId(), p.getName(), p.getAddress(),
                        p.getEmail(), p.getPhone(), p.getRegDate()));
            }
            return dtoList;
        } finally {
            session.close();
        }
    }

    @Override
    public String getNextPatientId() {
        Session session = SessionFactoryConfig.getInstance().getSession();
        try {
            String hql = "SELECT p.patientId FROM Patient p ORDER BY p.patientId DESC";
            String lastId = (String) session.createQuery(hql).setMaxResults(1).uniqueResult();
            if (lastId == null) return "P001";
            int idNum = Integer.parseInt(lastId.substring(1));
            return "P" + String.format("%03d", idNum + 1);
        } finally {
            session.close();
        }
    }

    // search patients with enrolled therapy programs
    @Override
    public List<String[]> searchPatientsWithPrograms(String keyword) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        try {
            String hql = "SELECT DISTINCT p.patientId, p.name, p.email, " +
                    "s.program.programName FROM Patient p " +
                    "JOIN p.sessions s " +
                    "WHERE lower(p.name) LIKE :kw OR lower(p.patientId) LIKE :kw " +
                    "ORDER BY p.name";
            List<Object[]> rows = session.createQuery(hql, Object[].class)
                    .setParameter("kw", "%" + keyword.toLowerCase() + "%")
                    .list();
            List<String[]> result = new ArrayList<>();
            for (Object[] row : rows) {
                result.add(new String[]{
                        String.valueOf(row[0]),  // patientId
                        String.valueOf(row[1]),  // name
                        String.valueOf(row[2]),  // email
                        String.valueOf(row[3])   // programName
                });
            }
            return result;
        } finally {
            session.close();
        }
    }
}