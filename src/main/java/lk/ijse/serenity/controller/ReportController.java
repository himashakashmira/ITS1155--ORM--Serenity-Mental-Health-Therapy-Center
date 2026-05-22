package lk.ijse.serenity.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lk.ijse.serenity.exception.PaymentException;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.ReportService;

public class ReportController {

    @FXML
    private Label lblStatus;

    private final ReportService reportService =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.REPORT);

    @FXML
    void btnPatientReportOnAction(ActionEvent event) {
        lblStatus.setText("Generating Patient Report...");
        lblStatus.setStyle("-fx-text-fill: #6b46c1;");
        new Thread(() -> {
            try {
                reportService.generatePatientReport();
                javafx.application.Platform.runLater(() -> {
                    lblStatus.setText("✅ Patient Report opened in PDF viewer.");
                    lblStatus.setStyle("-fx-text-fill: #38a169;");
                });
            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    lblStatus.setText("❌ Error: " + e.getMessage());
                    lblStatus.setStyle("-fx-text-fill: #e53e3e;");
                });
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    void btnSessionReportOnAction(ActionEvent event) {
        lblStatus.setText("Generating Session Report...");
        lblStatus.setStyle("-fx-text-fill: #6b46c1;");
        new Thread(() -> {
            try {
                reportService.generateSessionReport();
                javafx.application.Platform.runLater(() -> {
                    lblStatus.setText("✅ Session Report opened in PDF viewer.");
                    lblStatus.setStyle("-fx-text-fill: #38a169;");
                });
            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    lblStatus.setText("❌ Error: " + e.getMessage());
                    lblStatus.setStyle("-fx-text-fill: #e53e3e;");
                });
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    void btnPaymentReportOnAction(ActionEvent event) {
        lblStatus.setText("Generating Financial Report...");
        lblStatus.setStyle("-fx-text-fill: #6b46c1;");
        new Thread(() -> {
            try {
                reportService.generatePaymentReport();
                javafx.application.Platform.runLater(() -> {
                    lblStatus.setText("✅ Financial Report opened in PDF viewer.");
                    lblStatus.setStyle("-fx-text-fill: #38a169;");
                });
            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    lblStatus.setText("❌ Error: " + e.getMessage());
                    lblStatus.setStyle("-fx-text-fill: #e53e3e;");
                });
                e.printStackTrace();
            }
        }).start();
    }
}
