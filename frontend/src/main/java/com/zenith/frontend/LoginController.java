package com.zenith.frontend;

import com.zenith.frontend.api.AlertHelper;
import com.zenith.frontend.api.ApiClient;
import com.zenith.frontend.api.ApiException;
import com.zenith.frontend.api.AuthResponse;
import com.zenith.frontend.api.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

public class LoginController {

    @FXML private TextField     emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField     passwordVisible;
    @FXML private Button        eyeButton;
    @FXML private Button        loginButton;
    @FXML private Label         errorMessageLabel;
    @FXML private Region        errorSpacer;

    private boolean passwordShown = false;

    private void showError(String message) {
        errorMessageLabel.setText("⚠️ " + message);
        errorMessageLabel.setVisible(true);
        errorMessageLabel.setManaged(true);
        if (errorSpacer != null) {
            errorSpacer.setVisible(true);
            errorSpacer.setManaged(true);
        }
    }

    private void clearError() {
        if (errorMessageLabel != null) {
            errorMessageLabel.setText("");
            errorMessageLabel.setVisible(false);
            errorMessageLabel.setManaged(false);
        }
        if (errorSpacer != null) {
            errorSpacer.setVisible(false);
            errorSpacer.setManaged(false);
        }
    }

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
        clearError();

        String email    = emailField.getText().trim();
        String password = passwordShown
                ? passwordVisible.getText()
                : passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Email dan password wajib diisi.");
            return;
        }

        loginButton.setDisable(true);
        Parent root = loginButton.getScene().getRoot();

        new Thread(() -> {
            try {
                AuthResponse response = ApiClient.login(email, password);
                SessionManager.setSession(
                        response.getToken(),
                        response.getUserId(),
                        response.getFullName(),
                        response.getEmail());

                Platform.runLater(() -> {
                    loginButton.setDisable(false);
                    SceneNavigator.navigateWithAnimation("/dashboard.fxml", root, 60);
                });
            } catch (ApiException ex) {
                Platform.runLater(() -> {
                    loginButton.setDisable(false);
                    showError(ex.getMessage());
                });
            }
        }).start();
    }

    @FXML
    private void handleForgotPassword() {
        clearError();
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showError("Masukkan email Anda di atas terlebih dahulu untuk reset password.");
            return;
        }

        SessionManager.setResetEmail(email);
        Parent root = loginButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/forgot_password.fxml", root, 60);
    }

    @FXML
    private void handleRegister() {
        Parent root = loginButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/register.fxml", root, 60);
    }
}