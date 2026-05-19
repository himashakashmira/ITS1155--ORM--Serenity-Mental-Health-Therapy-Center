package lk.ijse.serenity.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.serenity.dto.UserDTO;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.AuthService;

import java.io.IOException;
import java.util.regex.Pattern;

public class LoginController {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblError;

    private final AuthService authService =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.AUTH);

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and Password cannot be empty.");
            return;
        }
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            showError("Username: 3-20 alphanumeric characters only.");
            return;
        }

        try {
            UserDTO loggedIn = authService.login(username, password);
            if (loggedIn != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardView.fxml"));
                Parent root = loader.load();
                DashboardController dashCtrl = loader.getController();
                dashCtrl.initDashboard(loggedIn);

                Stage stage = (Stage) txtUsername.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Serenity Therapy Center – Dashboard");
                stage.centerOnScreen();
            } else {
                showError("Invalid username or password.");
            }
        } catch (Exception e) {
            showError("Login error: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        lblError.setText(msg);
    }
}