package lk.ijse.serenity.service.custom.impl;

import lk.ijse.serenity.dao.DAOFactory;
import lk.ijse.serenity.dao.custom.PatientDAO;
import lk.ijse.serenity.dto.PatientDTO;
import lk.ijse.serenity.entity.Patient;
import lk.ijse.serenity.service.custom.PatientService;
import lk.ijse.serenity.util.SessionFactoryConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class PatientServiceImpl implements PatientService {
    // DAO Factory හරහා PatientDAO ලබා ගැනීම
    private final PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);

    @Override
    public boolean registerPatient(PatientDTO dto) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            // DTO එක Entity එකකට පරිවර්තනය කිරීම
            Patient patient = new Patient();
            patient.setPatientId(dto.getPatientId());
            patient.setName(dto.getName());
            patient.setAddress(dto.getAddress());
            patient.setEmail(dto.getEmail());
            patient.setPhone(dto.getPhone());

            // DAO එක හරහා Save කිරීම
            boolean isSaved = patientDAO.save(patient, session);

            if (isSaved) {
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
        } catch (Exception e) {
            transaction.rollback();
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
            List<Patient> allPatients = patientDAO.getAll(session);
            List<PatientDTO> dtos = new ArrayList<>();
            for (Patient p : allPatients) {
                dtos.add(new PatientDTO(p.getPatientId(), p.getName(), p.getAddress(), p.getEmail(), p.getPhone()));
            }
            return dtos;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean updatePatient(PatientDTO dto) {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Patient patient = session.get(Patient.class, dto.getPatientId());
            patient.setName(dto.getName());
            patient.setAddress(dto.getAddress());
            patient.setEmail(dto.getEmail());
            patient.setPhone(dto.getPhone());

            patientDAO.update(patient, session);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }
}