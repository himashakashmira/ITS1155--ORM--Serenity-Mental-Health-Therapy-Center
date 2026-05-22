package lk.ijse.serenity.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenity.dto.*;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TherapySessionController {

    @FXML
    private ComboBox<String> cmbPatient, cmbTherapist, cmbProgram, cmbStatus;
    @FXML
    private DatePicker dpDate;
    @FXML
    private Label lblStatus;
    @FXML
    private TableView<TherapySessionDTO> tblSession;
    @FXML
    private TableColumn<TherapySessionDTO, Integer> colId;
    @FXML
    private TableColumn<TherapySessionDTO, String> colPatient, colTherapist, colProgram, colStatus;
    @FXML
    private TableColumn<TherapySessionDTO, LocalDate> colDate;
    @FXML
    private Button btnBook;

    private int selectedSessionId = -1;

    private final TherapySessionService sessionService = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.SESSION);
    private final PatientService patientService = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PATIENT);
    private final TherapistService therapistService = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPIST);
    private final TherapyProgramService programService = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PROGRAM);

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colTherapist.setCellValueFactory(new PropertyValueFactory<>("therapistName"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadCombos();
        loadAllSessions();

        tblSession.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) setDataToFields(newValue);
        });
    }

    private void loadCombos() {
        try {
            cmbPatient.setItems(FXCollections.observableArrayList(patientService.getAllPatients().stream().map(PatientDTO::getPatientId).collect(Collectors.toList())));
            cmbTherapist.setItems(FXCollections.observableArrayList(therapistService.getAllTherapists().stream().map(t -> t.getTherapistId() + " - " + t.getName()).collect(Collectors.toList())));
            cmbProgram.setItems(FXCollections.observableArrayList(programService.getAllPrograms().stream().map(TherapyProgramDTO::getProgramId).collect(Collectors.toList())));
            cmbStatus.setItems(FXCollections.observableArrayList("Scheduled", "Completed", "Cancelled"));
        } catch (Exception e) {
            showStatus("Error loading dropdown data!", false);
        }
    }

    private void loadAllSessions() {
        try {
            List<TherapySessionDTO> all = sessionService.getAllSessions();
            tblSession.setItems(FXCollections.observableArrayList(all));
        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Error loading sessions!", false);
        }
    }

    private void setDataToFields(TherapySessionDTO dto) {
        selectedSessionId = dto.getSessionId();
        cmbPatient.setValue(dto.getPatientId());
        cmbTherapist.setValue(dto.getTherapistId());
        cmbProgram.setValue(dto.getProgramId());
        dpDate.setValue(dto.getSessionDate());
        cmbStatus.setValue(dto.getStatus());

        btnBook.setText("Update Session");
        cmbPatient.setDisable(true);
    }

    @FXML
    void btnBookOnAction(ActionEvent event) {
        if (validate()) {
            TherapySessionDTO dto = new TherapySessionDTO();
            dto.setSessionDate(dpDate.getValue());
            dto.setStatus(cmbStatus.getValue() == null ? "Scheduled" : cmbStatus.getValue());
            dto.setPatientId(cmbPatient.getValue());
            dto.setTherapistId(cmbTherapist.getValue());
            dto.setProgramId(cmbProgram.getValue());

            boolean result;
            if (btnBook.getText().equalsIgnoreCase("Update Session")) {
                dto.setSessionId(selectedSessionId);
                result = sessionService.updateSession(dto);
            } else {
                result = sessionService.bookSession(dto);
            }

            if (result) {
                showStatus("Success: Session recorded!", true);
                loadAllSessions();
                clear();
            } else {
                showStatus("Failed to process session.", false);
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        btnBookOnAction(event);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        btnBookOnAction(event);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (selectedSessionId == -1) {
            showStatus("Please select a session!", false);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete this appointment?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (sessionService.deleteSession(selectedSessionId)) {
                showStatus("Session deleted!", true);
                loadAllSessions();
                clear();
            }
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clear();
        lblStatus.setText("");
    }

    private boolean validate() {
        if (cmbPatient.getValue() == null || cmbTherapist.getValue() == null ||
                cmbProgram.getValue() == null || dpDate.getValue() == null) {
            showStatus("All fields are mandatory!", false);
            return false;
        }
        return true;
    }

    private void showStatus(String msg, boolean success) {
        lblStatus.setText(msg);
        lblStatus.setStyle("-fx-text-fill: " + (success ? "#38a169" : "#e53e3e") + ";");
    }

    private void clear() {
        selectedSessionId = -1;
        cmbPatient.getSelectionModel().clearSelection();
        cmbTherapist.getSelectionModel().clearSelection();
        cmbProgram.getSelectionModel().clearSelection();
        cmbStatus.getSelectionModel().clearSelection();
        dpDate.setValue(null);
        cmbPatient.setDisable(false);
        btnBook.setText("Book Session");
        tblSession.getSelectionModel().clearSelection();
    }
}