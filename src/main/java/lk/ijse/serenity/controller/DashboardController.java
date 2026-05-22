package lk.ijse.serenity.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.serenity.dto.UserDTO;

import java.io.IOException;

public class DashboardController {

    @FXML
    private AnchorPane contentArea;
    @FXML
    private Label lblWelcome;
    @FXML
    private Button btnTherapists;
    @FXML
    private Button btnPrograms;

    private UserDTO currentUser;

    public void initDashboard(UserDTO user) {
        this.currentUser = user;
        lblWelcome.setText("Welcome, " + user.getUsername() + " (" + user.getRole() + ")");

        boolean isAdmin = "ADMIN".equalsIgnoreCase(user.getRole());
        btnTherapists.setVisible(isAdmin);
        btnTherapists.setManaged(isAdmin);
        btnPrograms.setVisible(isAdmin);
        btnPrograms.setManaged(isAdmin);

        loadView("/view/DashboardHomeView.fxml");
    }

    @FXML
    void btnDashboardOnAction() {
        loadView("/view/DashboardHomeView.fxml");
    }

    @FXML
    void btnPatientsOnAction() {
        loadView("/view/PatientView.fxml");
    }

    @FXML
    void btnTherapistsOnAction() {
        loadView("/view/TherapistView.fxml");
    }

    @FXML
    void btnProgramsOnAction() {
        loadView("/view/TherapyProgramView.fxml");
    }

    @FXML
    void btnSessionsOnAction() {
        loadView("/view/TherapySession.fxml");
    }

    @FXML
    void btnPaymentsOnAction() {
        loadView("/view/PaymentView.fxml");
    }

    @FXML
    void btnLogoutOnAction() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Serenity Therapy Center – Login");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
