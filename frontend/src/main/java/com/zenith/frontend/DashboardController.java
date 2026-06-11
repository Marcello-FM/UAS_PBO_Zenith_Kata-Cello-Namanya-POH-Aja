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
        Parent root = cardRelax.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/relax_calm.fxml", root, 60);
    }

    @FXML
    private void handleCalmingGames() {
        Parent root = cardGames.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/calming_games.fxml", root, 60);
    }
}
