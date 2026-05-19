package lk.ijse.serenity.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenity.dto.PatientDTO;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.PatientService;

import java.util.List;

public class PatientController {
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtAddress;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;

    @FXML private TableView<PatientDTO> tblPatient;
    @FXML private TableColumn<?, ?> colId;
    @FXML private TableColumn<?, ?> colName;
    @FXML private TableColumn<?, ?> colAddress;
    @FXML private TableColumn<?, ?> colEmail;
    @FXML private TableColumn<?, ?> colPhone;

    // Service Factory from PatientService
    private final PatientService patientService = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PATIENT);

    public void initialize() {
        // Table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        loadAllPatients();

        // Table Row clicked fill the data from forms
        tblPatient.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setDataToFields(newValue);
            }
        });
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();

        if (id.isEmpty() || name.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "ID and Name are required!").show();
            return;
        }

        PatientDTO patientDTO = new PatientDTO(id, name, address, email, phone);

        boolean isSaved = patientService.registerPatient(patientDTO);

        if (isSaved) {
            new Alert(Alert.AlertType.INFORMATION, "Patient Registered Successfully!").show();
            btnClearOnAction(event);
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to register patient!").show();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtEmail.clear();
        txtPhone.clear();
    }

    private void loadAllPatients() {
        ObservableList<PatientDTO> observableList = FXCollections.observableArrayList();
        List<PatientDTO> allPatients = patientService.getAllPatients();
        observableList.addAll(allPatients);
        tblPatient.setItems(observableList);
    }

    private void setDataToFields(PatientDTO dto) {
        txtId.setText(dto.getPatientId());
        txtName.setText(dto.getName());
        txtAddress.setText(dto.getAddress());
        txtEmail.setText(dto.getEmail());
        txtPhone.setText(dto.getPhone());
        txtId.setEditable(false);
    }
}