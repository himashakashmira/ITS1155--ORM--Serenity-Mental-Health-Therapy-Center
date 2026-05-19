package lk.ijse.serenity.service.custom;

import lk.ijse.serenity.dto.PatientDTO;

import java.util.List;

public interface PatientService {
    boolean registerPatient(PatientDTO dto);

    List<PatientDTO> getAllPatients();

    boolean updatePatient(PatientDTO dto);
}