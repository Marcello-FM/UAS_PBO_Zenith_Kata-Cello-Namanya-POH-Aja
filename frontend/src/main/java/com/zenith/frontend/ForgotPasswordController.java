package com.zenith.frontend;

import com.zenith.frontend.api.AlertHelper;
import com.zenith.frontend.api.ApiClient;
import com.zenith.frontend.api.ApiException;
import com.zenith.frontend.api.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

public class ForgotPasswordController {

    @FXML private Label         emailLabel;
    @FXML private Label         errorMessageLabel;
    @FXML private Region        errorSpacer;

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
    private void initialize() {
        String email = SessionManager.getResetEmail();
        if (email != null && !email.isBlank()) {
        } else {
            showError("Email reset tidak ditemukan. Silakan kembali ke login.");
        }
    }

    private void showError(String message) {
        errorMessageLabel.setText("⚠️ " + message);
        errorMessageLabel.setVisible(true);
        errorMessageLabel.setManaged(true);
        errorSpacer.setVisible(true);
        errorSpacer.setManaged(true);
    }

    private void clearError() {
        errorMessageLabel.setText("");
        errorMessageLabel.setVisible(false);
        errorMessageLabel.setManaged(false);
        errorSpacer.setVisible(false);
        errorSpacer.setManaged(false);
    }

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
        clearError();

        String email = SessionManager.getResetEmail();
        String password = newPasswordShown
                ? newPasswordVisible.getText()
                : newPasswordField.getText();
        String confirm = confirmPasswordShown
                ? confirmPasswordVisible.getText()
                : confirmPasswordField.getText();

        if (email == null || email.isBlank()) {
            showError("Sesi telah habis. Silakan kembali ke halaman login.");
            return;
        }
        if (password.isEmpty() || confirm.isEmpty()) {
            showError("Semua field wajib diisi.");
            return;
        }
        if (!password.equals(confirm)) {
            showError("Password dan konfirmasi tidak cocok.");
            return;
        }
        if (password.length() < 6) {
            showError("Password harus memiliki minimal 6 karakter.");
            return;
        }

        createButton.setDisable(true);
        Parent root = createButton.getScene().getRoot();

        new Thread(() -> {
            try {
                ApiClient.resetPassword(email, password);
                Platform.runLater(() -> {
                    createButton.setDisable(false);
                    SessionManager.clearResetEmail();

                    AlertHelper.showInfo("Password berhasil diperbarui! Silakan login.");
                    SceneNavigator.navigateWithAnimation("/login.fxml", root, -60);
                });
            } catch (ApiException ex) {
                Platform.runLater(() -> {
                    createButton.setDisable(false);
                    showError(ex.getMessage());
                });
            }
        }).start();
    }

    @FXML
    private void handleBack() {
        Parent root = backButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/login.fxml", root, -60);
    }
}