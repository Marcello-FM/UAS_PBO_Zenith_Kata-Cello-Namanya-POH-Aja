package com.zenith.frontend;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Button relaxationButton;
    @FXML private Button questionnaireButton;
    @FXML private Button historyButton;
    @FXML private Button logoutButton;

    @FXML
    private void initialize() {
        welcomeLabel.setText("Zenith Dashboard");
    }

    @FXML
    private void handleRelaxation() {
        Parent root = relaxationButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/relaxation.fxml", root, 60);
    }

    @FXML
    private void handleQuestionnaire() {
        Parent root = questionnaireButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/questionnaire.fxml", root, 60);
    }

    @FXML
    private void handleHistory() {
        Parent root = historyButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/history.fxml", root, 60);
    }

    @FXML
    private void handleLogout() {
        Parent root = logoutButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/login.fxml", root, -60);
    }
}