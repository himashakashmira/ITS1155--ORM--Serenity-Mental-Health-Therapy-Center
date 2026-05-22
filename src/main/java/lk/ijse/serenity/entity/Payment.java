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
    @Column(name = "paymentId", length = 36)
    private String paymentId;
    private double amount;
    private LocalDate paymentDate;
    private String status;

    @OneToOne
    @JoinColumn(name = "sessionId")
    private TherapySession session;

    public Payment(String paymentId, double amount, String status, TherapySession session, LocalDate paymentDate) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = status;
        this.session = session;
        this.paymentDate = paymentDate;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TherapySession getSession() {
        return session;
    }

    public void setSession(TherapySession session) {
        this.session = session;
    }
}