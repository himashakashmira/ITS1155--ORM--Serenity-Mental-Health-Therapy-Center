package lk.ijse.serenity.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "therapy_program")
public class TherapyProgram {

    @Id
    @Column(name = "programId", length = 10)
    private String programId;

    @Column(nullable = false)
    private String programName;

    private String duration;

    private double fee;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<TherapySession> sessions;

    public TherapyProgram() {
    }

    public TherapyProgram(String programId, String programName, String duration, double fee, List<TherapySession> sessions) {
        this.programId = programId;
        this.programName = programName;
        this.duration = duration;
        this.fee = fee;
        this.sessions = sessions;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public List<TherapySession> getSessions() {
        return sessions;
    }

    public void setSessions(List<TherapySession> sessions) {
        this.sessions = sessions;
    }

    @Override
    public String toString() {
        return "TherapyProgram{" +
                "programId='" + programId + '\'' +
                ", programName='" + programName + '\'' +
                ", fee=" + fee +
                '}';
    }
}