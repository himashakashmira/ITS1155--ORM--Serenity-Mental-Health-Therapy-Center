package lk.ijse.serenity.service.custom;

import lk.ijse.serenity.dto.TherapySessionDTO;

import java.util.List;

public interface TherapySessionService {
    boolean bookSession(TherapySessionDTO dto);

    boolean updateSession(TherapySessionDTO dto);

    boolean deleteSession(int id);

    List<TherapySessionDTO> getAllSessions();
}