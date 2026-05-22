package lk.ijse.serenity.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lk.ijse.serenity.dto.UserDTO;
import lk.ijse.serenity.service.ServiceFactory;
import lk.ijse.serenity.service.custom.UserService;
import lk.ijse.serenity.util.PasswordUtil;

public class ChangePasswordController {

    @FXML
    private PasswordField txtCurrentPassword;
    @FXML
    private PasswordField txtNewPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private Label lblStatus;

    public static String loggedInUser;

    private final UserService userService =
            ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.USER);

    @FXML
    void btnUpdatePasswordOnAction(ActionEvent event) {
        String current = txtCurrentPassword.getText().trim();
        String newPwd = txtNewPassword.getText().trim();
        String confirm = txtConfirmPassword.getText().trim();

        // Validation
        if (current.isEmpty() || newPwd.isEmpty() || confirm.isEmpty()) {
            showStatus("All fields are required!", false);
            return;
        }

        if (newPwd.length() < 6) {
            showStatus("New password must be at least 6 characters!", false);
            return;
        }

        if (!newPwd.equals(confirm)) {
            showStatus("New passwords do not match!", false);
            return;
        }

        if (loggedInUser == null || loggedInUser.isEmpty()) {
            showStatus("Session error – please log in again.", false);
            return;
        }

        try {
            UserDTO user = userService.searchUser(loggedInUser);

            if (user == null) {
                showStatus("User not found in database!", false);
                return;
            }

            // Verify current password against BCrypt hash
            if (!PasswordUtil.checkPassword(current, user.getPassword())) {
                showStatus("Incorrect current password!", false);
                return;
            }

            user.setPassword(newPwd);

            if (userService.updateUser(user)) {
                showStatus("Password updated successfully!", true);
                clearFields();
            } else {
                showStatus("Update failed! Please try again.", false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Error: " + e.getMessage(), false);
        }
    }

    private void showStatus(String msg, boolean success) {
        lblStatus.setText(msg);
        lblStatus.setStyle("-fx-text-fill: " + (success ? "#38a169" : "#e53e3e")
                + "; -fx-font-weight: bold;");
    }

    private void clearFields() {
        txtCurrentPassword.clear();
        txtNewPassword.clear();
        txtConfirmPassword.clear();
    }
}