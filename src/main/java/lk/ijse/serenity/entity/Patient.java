package lk.ijse.serenity.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "patient")
public class Patient {
    @Id
    @Column(name = "patientId", length = 10)
    private String patientId;

    @Column(nullable = false)
    private String name;

    private String address;

    @Column(unique = true)
    private String email;

    private String phone;

    @Column(name = "regDate")
    private LocalDate regDate;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TherapySession> sessions;

    public Patient() {
    }

    public Patient(String patientId, String name, String address, String email, String phone, LocalDate regDate) {
        this.patientId = patientId;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.regDate = regDate;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

    public List<TherapySession> getSessions() {
        return sessions;
    }

    public void setSessions(List<TherapySession> sessions) {
        this.sessions = sessions;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}