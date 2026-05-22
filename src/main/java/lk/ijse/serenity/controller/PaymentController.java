package lk.ijse.serenity.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenity.dto.PaymentDTO;
import lk.ijse.serenity.dto.TherapySessionDTO;
import lk.ijse.serenity.exception.PaymentException;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.PaymentService;
import lk.ijse.serenity.service.custom.TherapySessionService;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PaymentController {

    @FXML
    private TextField txtPaymentId, txtAmount;
    @FXML
    private ComboBox<Integer> cmbSession;
    @FXML
    private ComboBox<String> cmbStatus;
    @FXML
    private DatePicker dpDate;
    @FXML
    private Label lblStatus;
    @FXML
    private TableView<PaymentDTO> tblPayment;
    @FXML
    private TableColumn<PaymentDTO, String> colPayId, colPatient, colProgram, colPayStatus;
    @FXML
    private TableColumn<PaymentDTO, Integer> colSessionId;
    @FXML
    private TableColumn<PaymentDTO, Double> colAmount;
    @FXML
    private TableColumn<PaymentDTO, LocalDate> colDate;

    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");

    private final PaymentService paymentService =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PAYMENT);
    private final TherapySessionService sessionService =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.SESSION);

    public void initialize() {
        colPayId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colSessionId.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPayStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        cmbStatus.setItems(FXCollections.observableArrayList("Paid", "Pending", "Refunded"));
        loadSessions();
        loadAllPayments();
        generatePaymentId();

        tblPayment.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) setDataToFields(val);
        });
    }

    private void generatePaymentId() {
        String shortId = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        txtPaymentId.setText(shortId);
        txtPaymentId.setEditable(false);
    }

    private void loadSessions() {
        try {
            cmbSession.setItems(FXCollections.observableArrayList(
                    sessionService.getAllSessions().stream()
                            .map(TherapySessionDTO::getSessionId)
                            .collect(Collectors.toList())));
        } catch (Exception e) {
            showStatus("Error loading sessions!", false);
        }
    }

    private void loadAllPayments() {
        try {
            tblPayment.setItems(FXCollections.observableArrayList(paymentService.getAllPayments()));
        } catch (Exception e) {
            showStatus("Error loading payments!", false);
        }
    }

    private void setDataToFields(PaymentDTO dto) {
        txtPaymentId.setText(dto.getPaymentId());
        txtPaymentId.setEditable(false);
        txtAmount.setText(String.valueOf(dto.getAmount()));
        dpDate.setValue(dto.getDate());
        cmbSession.setValue(dto.getSessionId());
        cmbStatus.setValue(dto.getStatus());
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validate()) return;
        try {
            PaymentDTO dto = buildDto();
            if (paymentService.savePayment(dto)) {
                showStatus("Payment Recorded Successfully!", true);
                loadAllPayments();
                clear();
            } else {
                showStatus("Payment Failed! Session may not exist or already paid.", false);
            }
        } catch (PaymentException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        } catch (Exception e) {
            showStatus("Invalid data! Check all fields.", false);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (txtPaymentId.getText().isEmpty()) {
            showStatus("Select a payment!", false);
            return;
        }
        if (!validate()) return;
        try {
            if (paymentService.updatePayment(buildDto())) {
                showStatus("Payment Updated Successfully!", true);
                loadAllPayments();
                clear();
            } else {
                showStatus("Update Failed!", false);
            }
        } catch (Exception e) {
            showStatus("Invalid data!", false);
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtPaymentId.getText();
        if (id.isEmpty()) {
            showStatus("Select a payment!", false);
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete payment " + id + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (paymentService.deletePayment(id)) {
                showStatus("Payment Deleted!", true);
                loadAllPayments();
                clear();
            }
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clear();
    }

    private PaymentDTO buildDto() {
        return new PaymentDTO(txtPaymentId.getText().trim(),
                cmbSession.getValue() != null ? cmbSession.getValue() : 0,
                "", "",
                Double.parseDouble(txtAmount.getText().trim()),
                dpDate.getValue() != null ? dpDate.getValue() : LocalDate.now(),
                cmbStatus.getValue());
    }

    private boolean validate() {
        if (cmbSession.getValue() == null) {
            showStatus("Please select a Session!", false);
            return false;
        }
        String amt = txtAmount.getText().trim();
        if (!AMOUNT_PATTERN.matcher(amt).matches()) {
            showStatus("Invalid Amount! Enter a number (e.g. 3500.00)", false);
            txtAmount.requestFocus();
            return false;
        }
        if (Double.parseDouble(amt) <= 0) {
            showStatus("Amount must be greater than 0!", false);
            return false;
        }
        if (dpDate.getValue() == null) {
            showStatus("Payment Date is required!", false);
            return false;
        }
        if (cmbStatus.getValue() == null) {
            showStatus("Please select a Status!", false);
            return false;
        }
        return true;
    }

    private void showStatus(String msg, boolean success) {
        lblStatus.setText(msg);
        lblStatus.setStyle("-fx-text-fill: " + (success ? "#38a169" : "#e53e3e") + ";");
    }

    private void clear() {
        txtAmount.clear();
        dpDate.setValue(LocalDate.now());
        cmbSession.getSelectionModel().clearSelection();
        cmbStatus.getSelectionModel().clearSelection();
        lblStatus.setText("");
        generatePaymentId();
    }
}