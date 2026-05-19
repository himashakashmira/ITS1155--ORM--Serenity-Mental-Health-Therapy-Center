package lk.ijse.serenity.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "therapist")
public class Therapist {
    @Id
    private String therapistId;
    private String name;
    private String specialization;
    private String phone;

    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL)
    private List<TherapySession> sessions;

    public Therapist() {
    }

    public String getTherapistId() {
        return therapistId;
    }

    public void setTherapistId(String therapistId) {
        this.therapistId = therapistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<TherapySession> getSessions() {
        return sessions;
    }

    public void setSessions(List<TherapySession> sessions) {
        this.sessions = sessions;
    }
}