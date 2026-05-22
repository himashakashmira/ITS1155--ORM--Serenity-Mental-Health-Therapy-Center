package lk.ijse.serenity.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenity.dto.TherapistDTO;
import lk.ijse.serenity.exception.RegistrationException;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.TherapistService;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class TherapistController {

    @FXML
    private TextField txtId, txtName, txtSpecialization, txtPhone, txtEmail;
    @FXML
    private Label lblStatus;
    @FXML
    private TableView<TherapistDTO> tblTherapist;
    @FXML
    private TableColumn<TherapistDTO, String> colId, colName, colSpec, colPhone, colEmail;

    private static final Pattern ID_PATTERN = Pattern.compile("^T\\d{3}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(07[0-9]{8}|\\+947[0-9]{8})$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z .]{2,60}$");

    private final TherapistService therapistService =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPIST);

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSpec.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadAllTherapists();

        tblTherapist.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) setData(val);
        });
    }

    private void loadAllTherapists() {
        try {
            List<TherapistDTO> all = therapistService.getAllTherapists();
            tblTherapist.setItems(FXCollections.observableArrayList(all));
        } catch (Exception e) {
            showStatus("Error loading therapists!", false);
        }
    }

    private void setData(TherapistDTO dto) {
        txtId.setText(dto.getTherapistId());
        txtName.setText(dto.getName());
        txtSpecialization.setText(dto.getSpecialization());
        txtPhone.setText(dto.getPhone());
        txtEmail.setText(dto.getEmail());
        txtId.setEditable(false);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validate()) return;
        TherapistDTO dto = new TherapistDTO(txtId.getText().trim(), txtName.getText().trim(),
                txtSpecialization.getText().trim(), txtPhone.getText().trim(), txtEmail.getText().trim());
        try {
            if (therapistService.saveTherapist(dto)) {
                showStatus("Therapist Saved Successfully!", true);
                loadAllTherapists();
                clear();
            } else {
                showStatus("Save Failed! ID may already exist.", false);
            }
        } catch (RegistrationException e) {
            showAlert("Registration Error", e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (txtId.getText().isEmpty()) {
            showStatus("Select a therapist!", false);
            return;
        }
        if (!validateForUpdate()) return;
        TherapistDTO dto = new TherapistDTO(txtId.getText().trim(), txtName.getText().trim(),
                txtSpecialization.getText().trim(), txtPhone.getText().trim(), txtEmail.getText().trim());
        if (therapistService.updateTherapist(dto)) {
            showStatus("Therapist Updated Successfully!", true);
            loadAllTherapists();
            clear();
        } else {
            showStatus("Update Failed!", false);
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();
        if (id.isEmpty()) {
            showStatus("Select a therapist first!", false);
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete Therapist " + id + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (therapistService.deleteTherapist(id)) {
                showStatus("Therapist Deleted!", true);
                loadAllTherapists();
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

    private boolean validate() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        if (!ID_PATTERN.matcher(id).matches()) {
            showStatus("Invalid Therapist ID! Eg: T001", false);
            txtId.requestFocus();
            return false;
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            showStatus("Invalid Name! Only letters (2-60 chars).", false);
            txtName.requestFocus();
            return false;
        }
        if (!email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
            showStatus("Invalid Email format!", false);
            txtEmail.requestFocus();
            return false;
        }
        if (!phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
            showStatus("Invalid Phone! Use 07XXXXXXXX", false);
            txtPhone.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateForUpdate() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        if (name.isEmpty()) {
            showStatus("Name is required!", false);
            return false;
        }
        if (!email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
            showStatus("Invalid Email!", false);
            return false;
        }
        if (!phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
            showStatus("Invalid Phone!", false);
            return false;
        }
        return true;
    }

    private void showStatus(String msg, boolean success) {
        lblStatus.setText(msg);
        lblStatus.setStyle("-fx-text-fill: " + (success ? "#38a169" : "#e53e3e") + ";");
    }

    private void showAlert(String title, String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }

    private void clear() {
        txtId.clear();
        txtName.clear();
        txtSpecialization.clear();
        txtPhone.clear();
        txtEmail.clear();
        txtId.setEditable(true);
        tblTherapist.getSelectionModel().clearSelection();
    }
}