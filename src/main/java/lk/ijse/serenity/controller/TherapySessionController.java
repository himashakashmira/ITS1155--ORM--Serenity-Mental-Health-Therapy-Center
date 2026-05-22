package lk.ijse.serenity.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenity.dto.*;
import lk.ijse.serenity.exception.SchedulingConflictException;
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
    private TextArea txaNotes;
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

    private final TherapySessionService sessionService =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.SESSION);
    private final PatientService patientService =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PATIENT);
    private final TherapistService therapistService =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPIST);
    private final TherapyProgramService programService =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PROGRAM);

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colTherapist.setCellValueFactory(new PropertyValueFactory<>("therapistName"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadCombos();
        loadAllSessions();

        tblSession.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) setDataToFields(val);
        });
    }

    private void loadCombos() {
        try {
            cmbPatient.setItems(FXCollections.observableArrayList(
                    patientService.getAllPatients().stream()
                            .map(p -> p.getPatientId() + " - " + p.getName())
                            .collect(Collectors.toList())));

            cmbTherapist.setItems(FXCollections.observableArrayList(
                    therapistService.getAllTherapists().stream()
                            .map(t -> t.getTherapistId() + " - " + t.getName())
                            .collect(Collectors.toList())));

            cmbProgram.setItems(FXCollections.observableArrayList(
                    programService.getAllPrograms().stream()
                            .map(p -> p.getProgramId() + " - " + p.getProgramName())
                            .collect(Collectors.toList())));

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
            showStatus("Error loading sessions!", false);
        }
    }

    private void setDataToFields(TherapySessionDTO dto) {
        selectedSessionId = dto.getSessionId();
        dpDate.setValue(dto.getSessionDate());
        cmbStatus.setValue(dto.getStatus());
        btnBook.setText("Update Session");
        cmbPatient.setDisable(true);

        findAndSetCombo(cmbPatient, dto.getPatientId());
        findAndSetCombo(cmbTherapist, dto.getTherapistId());
        findAndSetCombo(cmbProgram, dto.getProgramId());
    }

    private void findAndSetCombo(ComboBox<String> combo, String id) {
        if (id == null) return;
        combo.getItems().stream()
                .filter(item -> item.startsWith(id + " - ") || item.equals(id))
                .findFirst()
                .ifPresent(combo::setValue);
    }

    private String extractId(String comboValue) {
        if (comboValue == null) return null;
        return comboValue.contains(" - ") ? comboValue.split(" - ")[0].trim() : comboValue.trim();
    }

    @FXML
    void btnBookOnAction(ActionEvent event) {
        if (!validate()) return;
        TherapySessionDTO dto = new TherapySessionDTO();
        dto.setSessionDate(dpDate.getValue());
        dto.setStatus(cmbStatus.getValue() == null ? "Scheduled" : cmbStatus.getValue());
        dto.setPatientId(extractId(cmbPatient.getValue()));
        dto.setTherapistId(cmbTherapist.getValue());
        dto.setProgramId(extractId(cmbProgram.getValue()));

        try {
            boolean result;
            if (btnBook.getText().equalsIgnoreCase("Update Session")) {
                dto.setSessionId(selectedSessionId);
                result = sessionService.updateSession(dto);
            } else {
                result = sessionService.bookSession(dto);
            }
            if (result) {
                showStatus("Session recorded successfully!", true);
                loadAllSessions();
                clear();
            } else {
                showStatus("Failed to process session.", false);
            }
        } catch (SchedulingConflictException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            alert.setTitle("Scheduling Conflict");
            alert.setHeaderText("⚠ Session Conflict Detected");
            alert.showAndWait();
            showStatus(e.getMessage(), false);
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
            showStatus("Select a session!", false);
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete session #" + selectedSessionId + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (sessionService.deleteSession(selectedSessionId)) {
                showStatus("Session deleted!", true);
                loadAllSessions();
                clear();
            } else {
                showStatus("Delete failed!", false);
            }
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clear();
        lblStatus.setText("");
    }

    private boolean validate() {
        if (cmbPatient.getValue() == null) {
            showStatus("Please select a Patient!", false);
            return false;
        }
        if (cmbTherapist.getValue() == null) {
            showStatus("Please select a Therapist!", false);
            return false;
        }
        if (cmbProgram.getValue() == null) {
            showStatus("Please select a Program!", false);
            return false;
        }
        if (dpDate.getValue() == null) {
            showStatus("Session Date is required!", false);
            return false;
        }
        if (dpDate.getValue().isBefore(LocalDate.now()) && btnBook.getText().equals("Book Session")) {
            showStatus("Session date cannot be in the past!", false);
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
        if (txaNotes != null) txaNotes.clear();
        cmbPatient.setDisable(false);
        btnBook.setText("Book Session");
        tblSession.getSelectionModel().clearSelection();
    }
}