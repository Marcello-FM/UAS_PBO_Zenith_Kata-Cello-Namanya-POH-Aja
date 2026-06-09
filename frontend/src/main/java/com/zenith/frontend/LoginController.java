package com.zenith.frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField     emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField     passwordVisible;
    @FXML private Button        eyeButton;
    @FXML private Button        loginButton;

    private boolean passwordShown = false;

    @FXML
    private void togglePasswordVisibility() {
        if (passwordShown) {
            // hide: copy text back to PasswordField, show it, hide plain TextField
            passwordField.setText(passwordVisible.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordVisible.setVisible(false);
            passwordVisible.setManaged(false);
            eyeButton.setText("👁");
        } else {
            passwordVisible.setText(passwordField.getText());
            passwordVisible.setVisible(true);
            passwordVisible.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            eyeButton.setText("🙈");
        }
        passwordShown = !passwordShown;
    }

    @FXML
    private void handleLogin() {
        String email    = emailField.getText().trim();
        String password = passwordShown
                ? passwordVisible.getText()
                : passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Email and password are required.");
            return;
        }

        System.out.println("Logging in: " + email);
    }

    @FXML
    private void handleForgotPassword() {
        System.out.println("Forgot password");
    }

    @FXML
    private void handleRegister() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/register.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 720));
        } catch (Exception e) {
            // register screen not yet created
            System.out.println("Register screen not yet implemented.");
        }
    }
}
