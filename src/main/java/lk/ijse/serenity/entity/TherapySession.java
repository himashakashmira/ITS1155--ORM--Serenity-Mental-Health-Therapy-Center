package lk.ijse.serenity.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "therapy_session")
public class TherapySession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sessionId;

    private LocalDate sessionDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "patientId")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "therapistId")
    private Therapist therapist;

    @ManyToOne
    @JoinColumn(name = "programId")
    private TherapyProgram program;

    public TherapySession(LocalDate sessionDate, String status, Patient patient, Therapist therapist, TherapyProgram program) {
        this.sessionDate = sessionDate;
        this.status = status;
        this.patient = patient;
        this.therapist = therapist;
        this.program = program;
    }

    public TherapySession() {
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public TherapyProgram getProgram() {
        return program;
    }

    public void setProgram(TherapyProgram program) {
        this.program = program;
    }

    public Therapist getTherapist() {
        return therapist;
    }

    public void setTherapist(Therapist therapist) {
        this.therapist = therapist;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }
}