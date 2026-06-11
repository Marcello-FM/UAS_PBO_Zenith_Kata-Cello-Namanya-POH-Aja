package com.zenith.frontend;

import com.zenith.frontend.api.AlertHelper;
import com.zenith.frontend.api.ApiClient;
import com.zenith.frontend.api.ApiException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField     fullNameField;
    @FXML private TextField     emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField     passwordVisible;
    @FXML private Button        eyeBtn1;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField     confirmPasswordVisible;
    @FXML private Button        eyeBtn2;
    @FXML private CheckBox      termsCheck;
    @FXML private Button        registerButton;
    @FXML private Button        loginLink;

    private boolean passwordShown        = false;
    private boolean confirmPasswordShown = false;

    @FXML
    private void togglePassword() {
        if (passwordShown) {
            passwordField.setText(passwordVisible.getText());
            passwordField.setVisible(true);   passwordField.setManaged(true);
            passwordVisible.setVisible(false); passwordVisible.setManaged(false);
            eyeBtn1.setText("👁");
        } else {
            passwordVisible.setText(passwordField.getText());
            passwordVisible.setVisible(true);   passwordVisible.setManaged(true);
            passwordField.setVisible(false);    passwordField.setManaged(false);
            eyeBtn1.setText("🙈");
        }
        passwordShown = !passwordShown;
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
    private void handleRegister() {
        String name     = fullNameField.getText().trim();
        String email    = emailField.getText().trim();
        String password = passwordShown
                ? passwordVisible.getText()
                : passwordField.getText();
        String confirm  = confirmPasswordShown
                ? confirmPasswordVisible.getText()
                : confirmPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            AlertHelper.showError("Semua field wajib diisi.");
            return;
        }
        if (!password.equals(confirm)) {
            AlertHelper.showError("Password tidak cocok.");
            return;
        }
        if (!termsCheck.isSelected()) {
            AlertHelper.showError("Setujui terms & conditions terlebih dahulu.");
            return;
        }

        registerButton.setDisable(true);
        Parent root = registerButton.getScene().getRoot();

        new Thread(() -> {
            try {
                ApiClient.register(name, email, password);
                javafx.application.Platform.runLater(() -> {
                    registerButton.setDisable(false);
                    AlertHelper.showInfo("Registrasi berhasil. Silakan login.");
                    SceneNavigator.navigateWithAnimation("/login.fxml", root, -60);
                });
            } catch (ApiException ex) {
                javafx.application.Platform.runLater(() -> registerButton.setDisable(false));
                AlertHelper.showError(ex.getMessage());
            }
        }).start();
    }

    @FXML
    private void handleBackToLogin() {
        Parent root = loginLink.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/login.fxml", root, -60);
    }

    @FXML
    private void handleTerms() {
        AlertHelper.showInfo("Dengan menggunakan Zenith, kamu setuju menggunakan aplikasi ini untuk wellness pribadi.");
    }

    @FXML
    private void handlePrivacy() {
        AlertHelper.showInfo("Data kesehatan mentalmu disimpan secara aman di server lokal aplikasi.");
    }
}
