package lk.ijse.serenity.service.custom;

import lk.ijse.serenity.dto.TherapyProgramDTO;

import java.util.List;

public interface TherapyProgramService {
    boolean saveProgram(TherapyProgramDTO dto);

    boolean updateProgram(TherapyProgramDTO dto);

    boolean deleteProgram(String id);

    List<TherapyProgramDTO> getAllPrograms();
}