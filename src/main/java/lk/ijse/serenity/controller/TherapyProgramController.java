package lk.ijse.serenity.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenity.dto.TherapyProgramDTO;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.TherapyProgramService;

import java.util.Optional;
import java.util.regex.Pattern;

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

    private static final Pattern ID_PATTERN = Pattern.compile("^MT\\d{4}$");
    private static final Pattern FEE_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");

    private final TherapyProgramService service =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PROGRAM);

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
        if (!validate()) return;
        try {
            TherapyProgramDTO dto = new TherapyProgramDTO(txtId.getText().trim(),
                    txtName.getText().trim(), txtDuration.getText().trim(),
                    Double.parseDouble(txtFee.getText().trim()));
            if (service.saveProgram(dto)) {
                showStatus("Program Saved Successfully!", true);
                loadAll();
                clear();
            } else {
                showStatus("Save Failed! ID may already exist.", false);
            }
        } catch (NumberFormatException ex) {
            showStatus("Invalid fee amount!", false);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent e) {
        if (txtId.getText().isEmpty()) {
            showStatus("Select a program!", false);
            return;
        }
        try {
            TherapyProgramDTO dto = new TherapyProgramDTO(txtId.getText().trim(),
                    txtName.getText().trim(), txtDuration.getText().trim(),
                    Double.parseDouble(txtFee.getText().trim()));
            if (service.updateProgram(dto)) {
                showStatus("Program Updated Successfully!", true);
                loadAll();
                clear();
            } else {
                showStatus("Update Failed!", false);
            }
        } catch (NumberFormatException ex) {
            showStatus("Invalid fee amount!", false);
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent e) {
        String id = txtId.getText();
        if (id.isEmpty()) {
            showStatus("Select a program first!", false);
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete Program " + id + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (service.deleteProgram(id)) {
                showStatus("Program Deleted!", true);
                loadAll();
                clear();
            } else {
                showStatus("Delete Failed!", false);
            }
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent e) {
        clear();
        lblStatus.setText("");
    }

    private boolean validate() {
        String id = txtId.getText().trim();
        String fee = txtFee.getText().trim();
        if (!ID_PATTERN.matcher(id).matches()) {
            showStatus("Invalid Program ID! Eg: MT1001", false);
            txtId.requestFocus();
            return false;
        }
        if (txtName.getText().trim().isEmpty()) {
            showStatus("Program Name is required!", false);
            txtName.requestFocus();
            return false;
        }
        if (txtDuration.getText().trim().isEmpty()) {
            showStatus("Duration is required!", false);
            txtDuration.requestFocus();
            return false;
        }
        if (!FEE_PATTERN.matcher(fee).matches()) {
            showStatus("Invalid Fee! Enter a valid number (e.g. 80000.00)", false);
            txtFee.requestFocus();
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
        txtDuration.clear();
        txtFee.clear();
        txtId.setEditable(true);
        tblProgram.getSelectionModel().clearSelection();
    }
}