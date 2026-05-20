package lk.ijse.serenity.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenity.dto.PatientDTO;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.PatientService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PatientController {

    @FXML
    private TextField txtId, txtName, txtAddress, txtEmail, txtPhone;
    @FXML
    private DatePicker dpRegDate;
    @FXML
    private Label lblStatus;

    @FXML
    private TableView<PatientDTO> tblPatient;
    @FXML
    private TableColumn<PatientDTO, String> colId, colName, colAddress, colEmail, colPhone;
    @FXML
    private TableColumn<PatientDTO, LocalDate> colRegDate;

    private final PatientService patientService = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PATIENT);

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colRegDate.setCellValueFactory(new PropertyValueFactory<>("regDate"));

        loadAllPatients();

        tblPatient.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) setData(newValue);
        });
    }

    private void loadAllPatients() {
        try {
            List<PatientDTO> all = patientService.getAllPatients();
            tblPatient.setItems(FXCollections.observableArrayList(all));
        } catch (Exception e) {
            showStatus("Error loading patients!", false);
        }
    }

    private void setData(PatientDTO dto) {
        txtId.setText(dto.getPatientId());
        txtName.setText(dto.getName());
        txtAddress.setText(dto.getAddress());
        txtEmail.setText(dto.getEmail());
        txtPhone.setText(dto.getPhone());
        dpRegDate.setValue(dto.getRegDate());
        txtId.setEditable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (validate()) {
            PatientDTO dto = getDto();
            if (patientService.savePatient(dto)) {
                showStatus("Patient Registered Successfully!", true);
                loadAllPatients();
                clear();
            } else {
                showStatus("Registration Failed!", false);
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (txtId.isEditable()) {
            showStatus("Please select a patient to update!", false);
            return;
        }
        if (validate()) {
            if (patientService.updatePatient(getDto())) {
                showStatus("Patient Updated Successfully!", true);
                loadAllPatients();
                clear();
            }
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();
        if (id.isEmpty()) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Patient " + id + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (patientService.deletePatient(id)) {
                showStatus("Patient Deleted!", true);
                loadAllPatients();
                clear();
            }
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clear();
        lblStatus.setText("");
    }

    private PatientDTO getDto() {
        return new PatientDTO(txtId.getText(), txtName.getText(), txtAddress.getText(),
                txtEmail.getText(), txtPhone.getText(), dpRegDate.getValue());
    }

    private boolean validate() {
        // Regex Validation (Requirement 6)
        if (!txtId.getText().matches("^P[0-9]{3,}$")) {
            showStatus("Invalid ID! (Ex: P001)", false);
            return false;
        }
        if (txtName.getText().isEmpty() || dpRegDate.getValue() == null) {
            showStatus("Name and Date are required!", false);
            return false;
        }
        return true;
    }

    private void showStatus(String msg, boolean success) {
        lblStatus.setText(msg);
        lblStatus.setStyle("-fx-text-fill: " + (success ? "#38a169" : "#e53e3e") + ";");
    }

    private void clear() {
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtEmail.clear();
        txtPhone.clear();
        dpRegDate.setValue(null);
        txtId.setEditable(true);
        tblPatient.getSelectionModel().clearSelection();
    }
}