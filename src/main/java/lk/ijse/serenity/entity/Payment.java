package lk.ijse.serenity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    private String paymentId;
    private double amount;
    private LocalDate paymentDate;
    private String status;

    @OneToOne
    @JoinColumn(name = "sessionId")
    private TherapySession session;
}