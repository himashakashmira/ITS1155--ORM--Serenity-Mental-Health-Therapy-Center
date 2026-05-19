package lk.ijse.serenity.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenity.dto.TherapistDTO;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.TherapistService;

import java.util.List;

public class TherapistController {

    @FXML private TextField txtTherapistId;
    @FXML private TextField txtTherapistName;
    @FXML private TextField txtSpecialization;
    @FXML private TextField txtPhone;
    @FXML private TextField txtSearch;

    @FXML private TableView<TherapistDTO> tblTherapist;
    @FXML private TableColumn<TherapistDTO, String> colId;
    @FXML private TableColumn<TherapistDTO, String> colName;
    @FXML private TableColumn<TherapistDTO, String> colSpecialization;
    @FXML private TableColumn<TherapistDTO, String> colPhone;

    @FXML private Button btnSave;

    private final TherapistService therapistService = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.THERAPIST);

    public void initialize() {
        // 1. Table Columns
        colId.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        loadAllTherapists();

        tblTherapist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setDataToFields(newValue);
            }
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTherapists(newValue);
        });
    }

    private void loadAllTherapists() {
        List<TherapistDTO> allTherapists = therapistService.getAllTherapists();
        tblTherapist.setItems(FXCollections.observableArrayList(allTherapists));
    }

    private void setDataToFields(TherapistDTO therapist) {
        txtTherapistId.setText(therapist.getTherapistId());
        txtTherapistName.setText(therapist.getName());
        txtSpecialization.setText(therapist.getSpecialization());
        txtPhone.setText(therapist.getPhone());

        txtTherapistId.setEditable(false);
        btnSave.setText("Update Therapist");
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtTherapistId.getText();
        String name = txtTherapistName.getText();
        String spec = txtSpecialization.getText();
        String phone = txtPhone.getText();

        if (id.isEmpty() || name.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill required fields!").show();
            return;
        }

        TherapistDTO dto = new TherapistDTO(id, name, spec, phone);

        if (btnSave.getText().equalsIgnoreCase("Save Therapist")) {
            // SAVE logic
            if (therapistService.saveTherapist(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Therapist Saved!").show();
            }
        } else {
            // UPDATE logic (Service එකේ update method එක ලියන්න ඕන)
            // if (therapistService.updateTherapist(dto)) { ... }
        }

        loadAllTherapists();
        btnClearOnAction(event);
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        txtTherapistId.clear();
        txtTherapistName.clear();
        txtSpecialization.clear();
        txtPhone.clear();
        txtTherapistId.setEditable(true);
        btnSave.setText("Save Therapist");
    }

    private void filterTherapists(String searchText) {

    }
}