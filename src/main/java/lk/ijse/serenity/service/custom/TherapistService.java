package lk.ijse.serenity.service.custom;

import lk.ijse.serenity.dto.TherapistDTO;

import java.util.List;

public interface TherapistService {
    boolean saveTherapist(TherapistDTO dto);

    boolean updateTherapist(TherapistDTO dto);

    boolean deleteTherapist(String id);

    List<TherapistDTO> getAllTherapists();

    String getNextTherapistId();
}