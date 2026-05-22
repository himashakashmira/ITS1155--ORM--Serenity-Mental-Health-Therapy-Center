package lk.ijse.serenity.service.custom;

import lk.ijse.serenity.dto.PatientDTO;

import java.util.List;

public interface PatientService {
    boolean savePatient(PatientDTO dto);

    boolean updatePatient(PatientDTO dto);

    boolean deletePatient(String id);

    List<PatientDTO> getAllPatients();

    String getNextPatientId();

    List<String[]> searchPatientsWithPrograms(String keyword);  // HQL join
}