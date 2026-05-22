package lk.ijse.serenity.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenity.dto.PaymentDTO;
import lk.ijse.serenity.dto.TherapySessionDTO;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.PaymentService;
import lk.ijse.serenity.service.custom.TherapySessionService;

import java.time.LocalDate;
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

    private final PaymentService paymentService = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PAYMENT);
    private final TherapySessionService sessionService = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.SESSION);

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

        tblPayment.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setDataToFields(newValue);
            }
        });
    }

    private void loadSessions() {
        cmbSession.setItems(FXCollections.observableArrayList(
                sessionService.getAllSessions().stream().map(TherapySessionDTO::getSessionId).collect(Collectors.toList())
        ));
    }


    private void loadAllPayments() {
        tblPayment.setItems(FXCollections.observableArrayList(paymentService.getAllPayments()));
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
        try {
            PaymentDTO dto = new PaymentDTO(txtPaymentId.getText(), cmbSession.getValue(), "", "",
                    Double.parseDouble(txtAmount.getText()), dpDate.getValue(), cmbStatus.getValue());

            if (paymentService.savePayment(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Payment Recorded!").show();
                loadAllPayments();
                clear();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Invalid Data!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            PaymentDTO dto = new PaymentDTO(txtPaymentId.getText(), cmbSession.getValue(), "", "",
                    Double.parseDouble(txtAmount.getText()), dpDate.getValue(), cmbStatus.getValue());

            if (paymentService.updatePayment(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Payment Updated!").show();
                loadAllPayments();
                clear();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Invalid Data!").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (paymentService.deletePayment(txtPaymentId.getText())) {
            new Alert(Alert.AlertType.INFORMATION, "Deleted!").show();
            loadAllPayments();
            clear();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clear();
    }

    private void clear() {
        txtPaymentId.clear();
        txtAmount.clear();
        dpDate.setValue(null);
        cmbSession.getSelectionModel().clearSelection();
        cmbStatus.getSelectionModel().clearSelection();
    }
}