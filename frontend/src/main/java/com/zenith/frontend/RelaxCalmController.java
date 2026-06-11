package com.zenith.frontend;

import com.zenith.frontend.api.AlertHelper;
import com.zenith.frontend.api.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class RelaxCalmController {

    @FXML private HBox navHome;
    @FXML private HBox navGames;
    @FXML private HBox navStats;
    @FXML private HBox navLogout;

    @FXML private Button btnRain;
    @FXML private Button btnOcean;
    @FXML private Button btnForest;

    private MediaPlayer rainPlayer;
    private MediaPlayer oceanPlayer;
    private MediaPlayer forestPlayer;

    private boolean isRainPlaying = false;
    private boolean isOceanPlaying = false;
    private boolean isForestPlaying = false;

    @FXML
    private void initialize() {
        rainPlayer = createLoopPlayer("/audio/rain.mp3");
        oceanPlayer = createLoopPlayer("/audio/ocean.mp3");
        forestPlayer = createLoopPlayer("/audio/forest.mp3");
    }

    private MediaPlayer createLoopPlayer(String resourcePath) {
        var resource = getClass().getResource(resourcePath);
        if (resource == null) {
            AlertHelper.showError("File audio tidak ditemukan: " + resourcePath);
            return null;
        }

        MediaPlayer player = new MediaPlayer(new Media(resource.toExternalForm()));
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setVolume(0.75);
        return player;
    }

    @FXML
    private void handleHome() {
        stopAllSounds();
        Parent root = navHome.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/dashboard.fxml", root, -60);
    }

    @FXML
    private void handleCalmingGames() {
        stopAllSounds();
        Parent root = navHome.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/calming_games.fxml", root, 60);
    }

    @FXML
    private void handleStatistics() {
        stopAllSounds();
        Parent root = navStats.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/history.fxml", root, 60);
    }

    @FXML
    private void handleLogout() {
        stopAllSounds();
        SessionManager.clear();
        Parent root = navLogout.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/login.fxml", root, -60);
    }

    @FXML
    private void handlePlayRain() {
        isRainPlaying = toggleSound(rainPlayer, btnRain, isRainPlaying);
    }

    @FXML
    private void handlePlayOcean() {
        isOceanPlaying = toggleSound(oceanPlayer, btnOcean, isOceanPlaying);
    }

    @FXML
    private void handlePlayForest() {
        isForestPlaying = toggleSound(forestPlayer, btnForest, isForestPlaying);
    }

    private boolean toggleSound(MediaPlayer player, Button btn, boolean isPlaying) {
        if (player == null) {
            AlertHelper.showError("Audio belum siap. Coba restart aplikasi.");
            return false;
        }

        boolean nowPlaying = !isPlaying;
        if (nowPlaying) {
            player.play();
        } else {
            player.stop();
        }
        updateButtonState(btn, nowPlaying);
        return nowPlaying;
    }

    private void stopAllSounds() {
        isRainPlaying = stopSound(rainPlayer, btnRain, isRainPlaying);
        isOceanPlaying = stopSound(oceanPlayer, btnOcean, isOceanPlaying);
        isForestPlaying = stopSound(forestPlayer, btnForest, isForestPlaying);
    }

    private boolean stopSound(MediaPlayer player, Button btn, boolean isPlaying) {
        if (player != null && isPlaying) {
            player.stop();
            updateButtonState(btn, false);
            return false;
        }
        return isPlaying;
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
