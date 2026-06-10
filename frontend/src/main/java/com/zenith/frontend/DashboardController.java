package com.zenith.frontend;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DashboardController {

    @FXML private HBox navHome;
    @FXML private HBox navRelax;
    @FXML private HBox navStats;
    @FXML private HBox navProfile;
    @FXML private HBox navLogout;

    @FXML private VBox cardStressCheck;
    @FXML private VBox cardMyProgress;
    @FXML private VBox cardRelax;
    @FXML private VBox cardGames;

    @FXML
    private void handleLogout() {
        Parent root = navLogout.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/login.fxml", root, -60);
    }

    @FXML
    private void handleStressCheck() {
        Parent root = cardStressCheck.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/questionnaire.fxml", root, 60);
    }

    @FXML
    private void handleMyProgress() {
        System.out.println("My Progress");
    }

    @FXML
    private void handleRelax() {
        System.out.println("Relax & Calm");
    }

    @FXML
    private void handleCalmingGames() {
        System.out.println("Calming Games");
    }
}
