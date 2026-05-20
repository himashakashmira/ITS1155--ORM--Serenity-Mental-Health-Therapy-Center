package lk.ijse.serenity.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.serenity.dto.PaymentDTO;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.PaymentService;

import java.time.LocalDate;

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

    private final PaymentService service = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.PAYMENT);

    public void initialize() {
        colPayId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colSessionId.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colProgram.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPayStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        cmbStatus.setItems(FXCollections.observableArrayList("Paid", "Pending"));
        loadAll();
    }

    private void loadAll() {
        tblPayment.setItems(FXCollections.observableArrayList(service.getAll()));
    }

    @FXML
    void btnSaveOnAction(ActionEvent e) {
        PaymentDTO dto = new PaymentDTO(txtPaymentId.getText(), cmbSession.getValue(), "", "",
                Double.parseDouble(txtAmount.getText()), dpDate.getValue(), cmbStatus.getValue());
        if (service.savePayment(dto)) {
            new Alert(Alert.AlertType.INFORMATION, "Payment Recorded!").show();
            loadAll();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent e) {
        txtPaymentId.clear();
        txtAmount.clear();
        cmbSession.getSelectionModel().clearSelection();
        cmbStatus.getSelectionModel().clearSelection();
        dpDate.setValue(null);
    }
}