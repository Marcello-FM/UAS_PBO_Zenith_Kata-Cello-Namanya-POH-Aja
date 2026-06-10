package com.zenith.frontend;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
            passwordField.setText(passwordVisible.getText());
            passwordField.setVisible(true);    passwordField.setManaged(true);
            passwordVisible.setVisible(false); passwordVisible.setManaged(false);
            eyeButton.setText("👁");
        } else {
            passwordVisible.setText(passwordField.getText());
            passwordVisible.setVisible(true);  passwordVisible.setManaged(true);
            passwordField.setVisible(false);   passwordField.setManaged(false);
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

        Parent root = loginButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/dashboard.fxml", root, 60);
    }

    @FXML
    private void handleForgotPassword() {
        System.out.println("Forgot password");
    }

    @FXML
    private void handleRegister() {
        Parent root = loginButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/register.fxml", root, 60);
    }
}
