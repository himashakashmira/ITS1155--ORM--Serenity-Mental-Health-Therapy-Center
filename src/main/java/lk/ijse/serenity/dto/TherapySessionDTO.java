package lk.ijse.serenity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TherapySessionDTO {
    private int sessionId;
    private LocalDate sessionDate;
    private String status;
    private String patientId;
    private String therapistId;
    private String programId;

    // To table
    private String patientName;
    private String programName;
    private String therapistName;
}