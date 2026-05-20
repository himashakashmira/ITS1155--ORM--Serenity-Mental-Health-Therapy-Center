package lk.ijse.serenity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PatientDTO {
    private String PatientId;
    private String name;
    private String address;
    private String email;
    private String phone;
    private LocalDate RegDate;
}