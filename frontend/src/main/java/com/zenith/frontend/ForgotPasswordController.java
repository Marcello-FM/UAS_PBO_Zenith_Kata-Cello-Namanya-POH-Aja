package com.zenith.frontend;

import com.zenith.frontend.api.AlertHelper;
import com.zenith.frontend.api.ApiClient;
import com.zenith.frontend.api.ApiException;
import com.zenith.frontend.api.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ForgotPasswordController {

    @FXML private Label         emailLabel;
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
            emailLabel.setText("Reset password untuk: " + email);
        } else {
            emailLabel.setText("Email reset tidak ditemukan. Kembali ke login dan coba lagi.");
        }
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
        String email = SessionManager.getResetEmail();
        String password = newPasswordShown
                ? newPasswordVisible.getText()
                : newPasswordField.getText();
        String confirm = confirmPasswordShown
                ? confirmPasswordVisible.getText()
                : confirmPasswordField.getText();

        if (email == null || email.isBlank()) {
            AlertHelper.showError("Email reset tidak ditemukan.");
            return;
        }
        if (password.isEmpty() || confirm.isEmpty()) {
            AlertHelper.showError("Semua field wajib diisi.");
            return;
        }
        if (!password.equals(confirm)) {
            AlertHelper.showError("Password tidak cocok.");
            return;
        }
        if (password.length() < 6) {
            AlertHelper.showError("Password minimal 6 karakter.");
            return;
        }

        createButton.setDisable(true);
        Parent root = createButton.getScene().getRoot();

        new Thread(() -> {
            try {
                ApiClient.resetPassword(email, password);
                javafx.application.Platform.runLater(() -> {
                    createButton.setDisable(false);
                    SessionManager.clearResetEmail();
                    AlertHelper.showInfo("Password berhasil diperbarui.");
                    SceneNavigator.navigateWithAnimation("/login.fxml", root, -60);
                });
            } catch (ApiException ex) {
                javafx.application.Platform.runLater(() -> createButton.setDisable(false));
                AlertHelper.showError(ex.getMessage());
            }
        }).start();
    }

    @FXML
    private void handleBack() {
        Parent root = backButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/login.fxml", root, -60);
    }
}
