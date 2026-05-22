package lk.ijse.serenity.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.DashboardService;

import java.util.List;

public class DashboardHomeController {

    @FXML
    private Label lblTotalPatients;
    @FXML
    private Label lblTotalRevenue;
    @FXML
    private Label lblTodaySessions;
    @FXML
    private Label lblActiveTherapists;
    @FXML
    private Label lblPendingPayments;
    @FXML
    private ListView<String> lstSpecialPatients;

    private final DashboardService dashboardService =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.DASHBOARD);

    public void initialize() {
        loadStats();
    }

    private void loadStats() {
        try {
            long patientCount = dashboardService.getPatientCount();
            double revenue = dashboardService.getTotalRevenue();
            long todaySess = dashboardService.getTodaysSessionCount();
            long activeThera = dashboardService.getActiveTherapistCount();
            long pendingPay = dashboardService.getPendingPaymentCount();

            lblTotalPatients.setText(String.valueOf(patientCount));
            lblTotalRevenue.setText(String.format("%.2f", revenue));
            lblTodaySessions.setText(String.valueOf(todaySess));
            lblActiveTherapists.setText(String.valueOf(activeThera));
            lblPendingPayments.setText(String.valueOf(pendingPay));

            // patients enrolled in ALL programs
            List<String> specialPatients = dashboardService.getSpecialPatients();
            if (specialPatients.isEmpty()) {
                lstSpecialPatients.setItems(FXCollections.observableArrayList("No patient is enrolled in all programs yet."));
            } else {
                lstSpecialPatients.setItems(FXCollections.observableArrayList(specialPatients));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
