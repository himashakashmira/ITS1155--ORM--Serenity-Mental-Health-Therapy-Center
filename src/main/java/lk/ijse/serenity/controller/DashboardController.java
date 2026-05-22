package lk.ijse.serenity.controller;

import javafx.event.ActionEvent;
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

    @FXML private AnchorPane contentArea;
    @FXML private Label lblWelcome;
    @FXML private Button btnTherapists, btnPrograms;

    private UserDTO currentUser;

    public void initialize() {
        try {
            btnDashboardOnAction(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initDashboard(UserDTO userDTO) {
        this.currentUser = userDTO;
        if (userDTO != null) {
            lblWelcome.setText("Welcome, " + userDTO.getUsername() + " (" + userDTO.getRole() + ")");

            // hide Admin-only buttons for Receptionist
            if ("Receptionist".equalsIgnoreCase(userDTO.getRole())) {
                btnTherapists.setVisible(false);
                btnTherapists.setManaged(false);
                btnPrograms.setVisible(false);
                btnPrograms.setManaged(false);
            }
        }
        ChangePasswordController.loggedInUser = (userDTO != null) ? userDTO.getUsername() : null;
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) throws IOException {
        loadView("/view/DashboardHomeView.fxml");
    }

    @FXML
    void btnPatientsOnAction(ActionEvent event) throws IOException {
        loadView("/view/PatientView.fxml");
    }

    @FXML
    void btnTherapistsOnAction(ActionEvent event) throws IOException {
        loadView("/view/TherapistView.fxml");
    }

    @FXML
    void btnProgramsOnAction(ActionEvent event) throws IOException {
        loadView("/view/TherapyProgramView.fxml");
    }

    @FXML
    void btnSessionsOnAction(ActionEvent event) throws IOException {
        loadView("/view/TherapySession.fxml");
    }

    @FXML
    void btnPaymentsOnAction(ActionEvent event) throws IOException {
        loadView("/view/PaymentView.fxml");
    }

    @FXML
    void btnChangePasswordOnAction(ActionEvent event) throws IOException {
        loadView("/view/ChangePasswordView.fxml");
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) throws IOException {
        loadView("/view/ReportView.fxml");
    }

    @FXML
    void btnLogoutOnAction(ActionEvent event) throws IOException {
        currentUser = null;
        ChangePasswordController.loggedInUser = null;
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
        Stage stage = (Stage) contentArea.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Serenity Therapy Center – Login");
        stage.centerOnScreen();
    }

    private void loadView(String path) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource(path));
        contentArea.getChildren().clear();
        contentArea.getChildren().add(view);
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
    }
}