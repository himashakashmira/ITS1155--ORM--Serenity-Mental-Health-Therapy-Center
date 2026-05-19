package lk.ijse.serenity.service.custom;

import lk.ijse.serenity.dto.TherapistDTO;

import java.util.List;

public interface TherapistService {
    boolean saveTherapist(TherapistDTO dto);

    List<TherapistDTO> getAllTherapists();
}
