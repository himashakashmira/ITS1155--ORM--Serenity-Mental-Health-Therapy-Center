package lk.ijse.serenity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentDTO {
    private String paymentId;
    private int sessionId;
    private String patientName;
    private String programName;
    private double amount;
    private LocalDate date;
    private String status;
}