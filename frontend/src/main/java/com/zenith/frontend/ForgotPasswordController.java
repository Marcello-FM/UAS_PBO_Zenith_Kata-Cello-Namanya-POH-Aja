package com.zenith.frontend;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ForgotPasswordController {

    @FXML private PasswordField newPasswordField;
    @FXML private TextField     newPasswordVisible;
    @FXML private Button        eyeBtn1;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField     confirmPasswordVisible;
    @FXML private Button        eyeBtn2;
    @FXML private Button        backButton;
    @FXML private Button        createButton;

    private boolean newPasswordShown     = false;
    private boolean confirmPasswordShown = false;

    @FXML
    private void toggleNewPassword() {
        if (newPasswordShown) {
            newPasswordField.setText(newPasswordVisible.getText());
            newPasswordField.setVisible(true);    newPasswordField.setManaged(true);
            newPasswordVisible.setVisible(false); newPasswordVisible.setManaged(false);
            eyeBtn1.setText("👁");
        } else {
            newPasswordVisible.setText(newPasswordField.getText());
            newPasswordVisible.setVisible(true);  newPasswordVisible.setManaged(true);
            newPasswordField.setVisible(false);   newPasswordField.setManaged(false);
            eyeBtn1.setText("🙈");
        }
        newPasswordShown = !newPasswordShown;
    }

    @FXML
    private void toggleConfirmPassword() {
        if (confirmPasswordShown) {
            confirmPasswordField.setText(confirmPasswordVisible.getText());
            confirmPasswordField.setVisible(true);   confirmPasswordField.setManaged(true);
            confirmPasswordVisible.setVisible(false); confirmPasswordVisible.setManaged(false);
            eyeBtn2.setText("👁");
        } else {
            confirmPasswordVisible.setText(confirmPasswordField.getText());
            confirmPasswordVisible.setVisible(true);   confirmPasswordVisible.setManaged(true);
            confirmPasswordField.setVisible(false);    confirmPasswordField.setManaged(false);
            eyeBtn2.setText("🙈");
        }
        confirmPasswordShown = !confirmPasswordShown;
    }

    @FXML
    private void handleCreatePassword() {
        String password = newPasswordShown
                ? newPasswordVisible.getText()
                : newPasswordField.getText();
        String confirm = confirmPasswordShown
                ? confirmPasswordVisible.getText()
                : confirmPasswordField.getText();

        if (password.isEmpty() || confirm.isEmpty()) {
            System.out.println("Both fields are required.");
            return;
        }
        if (!password.equals(confirm)) {
            System.out.println("Passwords do not match.");
            return;
        }

        System.out.println("Password reset successful.");

        Parent root = createButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/login.fxml", root, -60);
    }

    @FXML
    private void handleBack() {
        Parent root = backButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/login.fxml", root, -60);
    }
}
