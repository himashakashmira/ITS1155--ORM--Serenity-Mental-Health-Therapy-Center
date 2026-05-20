package lk.ijse.serenity.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenity.dto.TherapyProgramDTO;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.TherapyProgramService;

public class TherapyProgramController {
    @FXML
    private TextField txtId, txtName, txtDuration, txtFee;
    @FXML
    private Label lblStatus;
    @FXML
    private TableView<TherapyProgramDTO> tblProgram;
    @FXML
    private TableColumn<TherapyProgramDTO, String> colId, colName, colDuration;
    @FXML
    private TableColumn<TherapyProgramDTO, Double> colFee;

    private final TherapyProgramService service = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PROGRAM);

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("programId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        loadAll();
        tblProgram.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) {
                txtId.setText(val.getProgramId());
                txtName.setText(val.getProgramName());
                txtDuration.setText(val.getDuration());
                txtFee.setText(String.valueOf(val.getFee()));
                txtId.setEditable(false);
            }
        });
    }

    private void loadAll() {
        tblProgram.setItems(FXCollections.observableArrayList(service.getAllPrograms()));
    }

    @FXML
    void btnSaveOnAction(ActionEvent e) {
        if (service.saveProgram(new TherapyProgramDTO(txtId.getText(), txtName.getText(), txtDuration.getText(), Double.parseDouble(txtFee.getText())))) {
            new Alert(Alert.AlertType.INFORMATION, "Saved!").show();
            loadAll();
            clear();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent e) {
        if (service.updateProgram(new TherapyProgramDTO(txtId.getText(), txtName.getText(), txtDuration.getText(), Double.parseDouble(txtFee.getText())))) {
            new Alert(Alert.AlertType.INFORMATION, "Updated!").show();
            loadAll();
            clear();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent e) {
        if (service.deleteProgram(txtId.getText())) {
            new Alert(Alert.AlertType.INFORMATION, "Deleted!").show();
            loadAll();
            clear();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent e) {
        clear();
    }

    private void clear() {
        txtId.clear();
        txtName.clear();
        txtDuration.clear();
        txtFee.clear();
        txtId.setEditable(true);
    }
}