package lk.ijse.serenity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TherapyProgramDTO {
    private String programId;
    private String programName;
    private String duration;
    private double fee;
}