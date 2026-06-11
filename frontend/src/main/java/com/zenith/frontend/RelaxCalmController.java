package com.zenith.frontend;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class RelaxCalmController {

    @FXML private HBox navHome;
    @FXML private HBox navGames;
    @FXML private HBox navLogout;

    @FXML private Button btnRain;
    @FXML private Button btnOcean;
    @FXML private Button btnForest;

    private boolean isRainPlaying = false;
    private boolean isOceanPlaying = false;
    private boolean isForestPlaying = false;

    @FXML
    private void handleHome() {
        Parent root = navHome.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/dashboard.fxml", root, -60);
    }

    @FXML
    private void handleCalmingGames() {
        Parent root = navHome.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/calming_games.fxml", root, 60);
    }

    @FXML
    private void handleLogout() {
        Parent root = navLogout.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/login.fxml", root, -60);
    }

    @FXML
    private void handlePlayRain() {
        isRainPlaying = !isRainPlaying;
        updateButtonState(btnRain, isRainPlaying);
    }

    @FXML
    private void handlePlayOcean() {
        isOceanPlaying = !isOceanPlaying;
        updateButtonState(btnOcean, isOceanPlaying);
    }

    @FXML
    private void handlePlayForest() {
        isForestPlaying = !isForestPlaying;
        updateButtonState(btnForest, isForestPlaying);
    }

    private void updateButtonState(Button btn, boolean isPlaying) {
        if (isPlaying) {
            btn.setText("⏸ Sedang Diputar");
            btn.setStyle("-fx-background-color: #7dd8f0; -fx-text-fill: #1a3a4a; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 12 24; -fx-cursor: hand;");
        } else {
            btn.setText("▶ Putar Suara");
            btn.setStyle("-fx-background-color: #c8e6f5; -fx-text-fill: #1a3a4a; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 12 24; -fx-cursor: hand;");
        }
    }
}