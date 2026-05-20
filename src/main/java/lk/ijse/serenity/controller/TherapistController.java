package lk.ijse.serenity.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenity.dto.TherapistDTO;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.TherapistService;

import java.util.List;

public class TherapistController {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtSpecialization;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtEmail;
    @FXML
    private Label lblStatus;

    @FXML
    private TableView<TherapistDTO> tblTherapist;
    @FXML
    private TableColumn<TherapistDTO, String> colId;
    @FXML
    private TableColumn<TherapistDTO, String> colName;
    @FXML
    private TableColumn<TherapistDTO, String> colSpec;
    @FXML
    private TableColumn<TherapistDTO, String> colPhone;
    @FXML
    private TableColumn<TherapistDTO, String> colEmail;

    private final TherapistService therapistService = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPIST);

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSpec.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadAllTherapists();

        // select the table fill the field
        tblTherapist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setData(newValue);
            }
        });
    }

    private void loadAllTherapists() {
        try {
            List<TherapistDTO> all = therapistService.getAllTherapists();
            tblTherapist.setItems(FXCollections.observableArrayList(all));
        } catch (Exception e) {
            lblStatus.setText("Error loading data!");
            lblStatus.setStyle("-fx-text-fill: #e53e3e;");
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
        String id = txtId.getText();
        String name = txtName.getText();
        String spec = txtSpecialization.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();

        if (!id.matches("^T[0-9]{3,}$")) {
            lblStatus.setText("Invalid ID! Use T001 format.");
            lblStatus.setStyle("-fx-text-fill: #e53e3e;");
            return;
        }

        TherapistDTO dto = new TherapistDTO(id, name, spec, phone, email);

        boolean isSaved = therapistService.saveTherapist(dto);

        if (isSaved) {
            lblStatus.setText("Therapist record updated successfully!");
            lblStatus.setStyle("-fx-text-fill: #38a169;");
            loadAllTherapists();
            clear();
        } else {
            lblStatus.setText("Failed to save therapist.");
            lblStatus.setStyle("-fx-text-fill: #e53e3e;");
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clear();
        lblStatus.setText("");
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