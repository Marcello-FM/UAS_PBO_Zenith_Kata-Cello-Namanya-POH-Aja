package com.zenith.frontend;

import com.zenith.frontend.api.AlertHelper;
import com.zenith.frontend.api.ApiClient;
import com.zenith.frontend.api.ApiException;
import com.zenith.frontend.api.AuthResponse;
import com.zenith.frontend.api.SessionManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Optional;

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
            AlertHelper.showError("Email dan password wajib diisi.");
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

                javafx.application.Platform.runLater(() -> {
                    loginButton.setDisable(false);
                    SceneNavigator.navigateWithAnimation("/dashboard.fxml", root, 60);
                });
            } catch (ApiException ex) {
                javafx.application.Platform.runLater(() -> {
                    loginButton.setDisable(false);
                    AlertHelper.showError(ex.getMessage());
                });
            }
        }).start();
    }

    @FXML
    private void handleForgotPassword() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            AlertHelper.showError("Masukkan email terlebih dahulu.");
            return;
        }

        showResetPasswordDialog(email);
    }

    private void showResetPasswordDialog(String email) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Reset Password");
        dialog.setHeaderText("Buat password baru");
        dialog.setContentText("Email: " + email);

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Password baru (min. 6 karakter)");
        newPasswordField.setMaxWidth(Double.MAX_VALUE);

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Konfirmasi password");
        confirmPasswordField.setMaxWidth(Double.MAX_VALUE);

        VBox content = new VBox(8,
                new Label("Password Baru"),
                newPasswordField,
                new Label("Konfirmasi Password"),
                confirmPasswordField);
        content.setPadding(new Insets(10, 0, 0, 0));
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (newPassword.isBlank() || confirmPassword.isBlank()) {
            AlertHelper.showError("Semua field wajib diisi.");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            AlertHelper.showError("Password tidak cocok.");
            return;
        }
        if (newPassword.length() < 6) {
            AlertHelper.showError("Password minimal 6 karakter.");
            return;
        }

        new Thread(() -> {
            try {
                ApiClient.resetPassword(email, newPassword);
                javafx.application.Platform.runLater(() ->
                        AlertHelper.showInfo("Password berhasil diperbarui. Silakan login."));
            } catch (ApiException ex) {
                javafx.application.Platform.runLater(() -> AlertHelper.showError(ex.getMessage()));
            }
        }).start();
    }

    @FXML
    private void handleRegister() {
        Parent root = loginButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/register.fxml", root, 60);
    }
}
