package lk.ijse.serenity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TherapistDTO {
    private String therapistId;
    private String name;
    private String specialization;
    private String phone;
    private String email;
}