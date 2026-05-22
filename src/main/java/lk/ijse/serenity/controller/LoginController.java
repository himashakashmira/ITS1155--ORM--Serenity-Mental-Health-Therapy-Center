package lk.ijse.serenity.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.serenity.dto.UserDTO;
import lk.ijse.serenity.exception.LoginException;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.UserService;
import lk.ijse.serenity.util.PasswordUtil;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblError;
    @FXML
    private CheckBox chkShowPassword;
    @FXML
    private TextField txtPasswordPlain;

    private final UserService userService =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.USER);

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = chkShowPassword.isSelected()
                ? txtPasswordPlain.getText()
                : txtPassword.getText();

        lblError.setVisible(false);

        try {
            if (username.isEmpty() || password.isEmpty()) {
                throw new LoginException("Username and password are required!");
            }

            UserDTO userDTO = userService.searchUser(username);

            if (userDTO == null || !PasswordUtil.checkPassword(password, userDTO.getPassword())) {
                throw new LoginException("Invalid username or password!");
            }

            // Login Success
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardView.fxml"));
            Parent root = loader.load();

            DashboardController dashCtrl = loader.getController();
            dashCtrl.initDashboard(userDTO);

            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Serenity Therapy Center – Dashboard");
            stage.setWidth(1200);
            stage.setHeight(800);
            stage.centerOnScreen();
            stage.show();

        } catch (LoginException e) {
            lblError.setText("⚠ " + e.getMessage());
            lblError.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            lblError.setText("System Error: Could not load dashboard.");
            lblError.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            lblError.setText("An unexpected error occurred.");
            lblError.setVisible(true);
        }
    }

    @FXML
    void chkShowPasswordOnAction(ActionEvent event) {
        if (chkShowPassword.isSelected()) {
            txtPasswordPlain.setText(txtPassword.getText());
            txtPasswordPlain.setVisible(true);
            txtPassword.setVisible(false);
        } else {
            txtPassword.setText(txtPasswordPlain.getText());
            txtPassword.setVisible(true);
            txtPasswordPlain.setVisible(false);
        }
    }
}