package lk.ijse.serenity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "patient")
public class Patient {
    @Id
    private String patientId;
    private String name;
    private String address;
    private String email;
    private String phone;
    private LocalDate RegDate;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<TherapySession> sessions;
}