package lk.ijse.serenity.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenity.dto.PatientDTO;
import lk.ijse.serenity.exception.RegistrationException;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.PatientService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class PatientController {

    @FXML
    private TextField txtId, txtName, txtAddress, txtEmail, txtPhone, txtSearch;
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

    private static final Pattern ID_PATTERN = Pattern.compile("^P\\d{3}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(07[0-9]{8}|\\+947[0-9]{8})$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z ]{2,60}$");

    private final PatientService patientService =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PATIENT);

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colRegDate.setCellValueFactory(new PropertyValueFactory<>("regDate"));

        loadAllPatients();
        autoSetNextId();

        tblPatient.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) setData(val);
        });
    }

    private void autoSetNextId() {
        try {
            txtId.setText(patientService.getNextPatientId());
            txtId.setEditable(false);
        } catch (Exception ignored) {
        }
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
        txtId.setEditable(false);
        txtName.setText(dto.getName());
        txtAddress.setText(dto.getAddress());
        txtEmail.setText(dto.getEmail());
        txtPhone.setText(dto.getPhone());
        dpRegDate.setValue(dto.getRegDate());
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            showStatus("Enter a name or ID to search.", false);
            return;
        }
        try {
            List<String[]> results = patientService.searchPatientsWithPrograms(keyword);
            if (results.isEmpty()) {
                showStatus("No results found for: " + keyword, false);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-10s %-25s %-30s %-30s\n", "ID", "Name", "Email", "Program"));
            sb.append("-".repeat(95)).append("\n");
            for (String[] row : results) {
                sb.append(String.format("%-10s %-25s %-30s %-30s\n", row[0], row[1], row[2], row[3]));
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Patient Search Results");
            alert.setHeaderText("Patients matching: \"" + keyword + "\"");
            TextArea area = new TextArea(sb.toString());
            area.setEditable(false);
            area.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
            area.setPrefSize(700, 300);
            alert.getDialogPane().setContent(area);
            alert.getDialogPane().setPrefWidth(750);
            alert.showAndWait();
            showStatus(results.size() + " result(s) found.", true);
        } catch (Exception e) {
            showStatus("Search error: " + e.getMessage(), false);
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validate()) return;
        try {
            PatientDTO dto = getDto();
            if (patientService.savePatient(dto)) {
                showStatus("Patient Registered Successfully!", true);
                loadAllPatients();
                clear();
            } else {
                showStatus("Registration Failed! Check ID.", false);
            }
        } catch (RegistrationException e) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", e.getMessage());
            showStatus(e.getMessage(), false);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (txtId.getText().isEmpty()) {
            showStatus("Please select a patient to update!", false);
            return;
        }
        if (validateForUpdate()) {
            if (patientService.updatePatient(getDto())) {
                showStatus("Patient Updated Successfully!", true);
                loadAllPatients();
                clear();
            } else {
                showStatus("Update Failed!", false);
            }
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();
        if (id.isEmpty()) {
            showStatus("Select a patient first!", false);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete Patient " + id + "? This will also remove their sessions.", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirm Delete");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (patientService.deletePatient(id)) {
                showStatus("Patient Deleted!", true);
                loadAllPatients();
                clear();
            } else {
                showStatus("Delete Failed!", false);
            }
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clear();
        lblStatus.setText("");
    }

    private PatientDTO getDto() {
        return new PatientDTO(txtId.getText().trim(), txtName.getText().trim(),
                txtAddress.getText().trim(), txtEmail.getText().trim(),
                txtPhone.getText().trim(), dpRegDate.getValue());
    }

    private boolean validate() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        if (!ID_PATTERN.matcher(id).matches()) {
            showStatus("Invalid Patient ID! Eg: P001, P002 ...", false);
            txtId.requestFocus();
            return false;
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            showStatus("Invalid Name! Only letters and spaces (2-60 chars).", false);
            txtName.requestFocus();
            return false;
        }
        if (!email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
            showStatus("Invalid Email! Eg: example@gmail.com", false);
            txtEmail.requestFocus();
            return false;
        }
        if (!phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
            showStatus("Invalid Phone! Eg: 07XXXXXXXX or +947XXXXXXXX", false);
            txtPhone.requestFocus();
            return false;
        }
        if (dpRegDate.getValue() == null) {
            showStatus("Registration Date is required!", false);
            return false;
        }
        return true;
    }

    private boolean validateForUpdate() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        if (!NAME_PATTERN.matcher(name).matches()) {
            showStatus("Invalid Name!", false);
            return false;
        }
        if (!email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
            showStatus("Invalid Email!", false);
            return false;
        }
        if (!phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
            showStatus("Invalid Phone! Use 07XXXXXXXX", false);
            return false;
        }
        return true;
    }

    private void showStatus(String msg, boolean success) {
        lblStatus.setText(msg);
        lblStatus.setStyle("-fx-text-fill: " + (success ? "#38a169" : "#e53e3e") + ";");
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type, msg, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    private void clear() {
        txtId.setEditable(true);
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtEmail.clear();
        txtPhone.clear();
        dpRegDate.setValue(null);
        tblPatient.getSelectionModel().clearSelection();
        autoSetNextId();
    }
}