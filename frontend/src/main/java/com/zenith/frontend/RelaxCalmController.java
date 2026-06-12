package com.zenith.frontend;

import com.zenith.frontend.api.AlertHelper;
import com.zenith.frontend.api.SessionManager;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class RelaxCalmController {

    @FXML private HBox navHome;
    @FXML private HBox navGames;
    @FXML private HBox navStats;
    @FXML private HBox navLogout;

    @FXML private VBox cardRain;
    @FXML private VBox cardOcean;
    @FXML private VBox cardForest;

    @FXML private Button btnRain;
    @FXML private Button btnOcean;
    @FXML private Button btnForest;

    private MediaPlayer rainPlayer;
    private MediaPlayer oceanPlayer;
    private MediaPlayer forestPlayer;

    private boolean isRainPlaying = false;
    private boolean isOceanPlaying = false;
    private boolean isForestPlaying = false;

    private static final String STYLE_CARD_DEFAULT = "-fx-background-color: white; -fx-background-radius: 30; -fx-border-color: #d1d5db; -fx-border-width: 0.5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 15, 0, 0, 5);";
    private static final String STYLE_HOVER_RAIN = "-fx-background-color: #4dd0e1; -fx-background-radius: 30; -fx-effect: dropshadow(gaussian, rgba(77,208,225,0.4), 20, 0, 0, 8);";
    private static final String STYLE_HOVER_OCEAN = "-fx-background-color: #64b5f6; -fx-background-radius: 30; -fx-effect: dropshadow(gaussian, rgba(100,181,246,0.4), 20, 0, 0, 8);";
    private static final String STYLE_HOVER_FOREST = "-fx-background-color: #81c784; -fx-background-radius: 30; -fx-effect: dropshadow(gaussian, rgba(129,199,132,0.4), 20, 0, 0, 8);";

    @FXML
    private void initialize() {
        rainPlayer = createLoopPlayer("/audio/rain.mp3");
        oceanPlayer = createLoopPlayer("/audio/ocean.mp3");
        forestPlayer = createLoopPlayer("/audio/forest.mp3");

        Platform.runLater(() -> {
            setupCardInteraction(cardRain, STYLE_HOVER_RAIN);
            setupCardInteraction(cardOcean, STYLE_HOVER_OCEAN);
            setupCardInteraction(cardForest, STYLE_HOVER_FOREST);
        });
    }

    private void setupCardInteraction(VBox card, String hoverStyle) {
        if (card == null) return;
        ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
        st.setInterpolator(Interpolator.EASE_BOTH);

        card.setOnMouseEntered(e -> {
            st.stop();
            card.setStyle(hoverStyle);
            st.setToX(1.08);
            st.setToY(1.08);
            card.setViewOrder(-1.0);
            st.play();
        });

        card.setOnMouseExited(e -> {
            st.stop();
            card.setStyle(STYLE_CARD_DEFAULT);
            st.setToX(1.0);
            st.setToY(1.0);
            card.setViewOrder(0.0);
            st.play();
        });
    }

    private MediaPlayer createLoopPlayer(String resourcePath) {
        var resource = getClass().getResource(resourcePath);
        if (resource == null) {
            AlertHelper.showError("File audio tidak ditemukan: " + resourcePath);
            return null;
        }

        MediaPlayer player = new MediaPlayer(new Media(resource.toExternalForm()));
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setVolume(0.7);
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
        Parent root = navGames.getScene().getRoot();
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
        if (rainPlayer != null) rainPlayer.stop();
        if (oceanPlayer != null) oceanPlayer.stop();
        if (forestPlayer != null) forestPlayer.stop();
    }

    private void updateButtonState(Button btn, boolean isPlaying) {
        if (isPlaying) {
            btn.setText("⏸ Pause");
            btn.setStyle("-fx-background-color: #7dd8f0; -fx-text-fill: #1a3a4a; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 14 32;");
        } else {
            btn.setText("▶ Play");
            btn.setStyle("-fx-background-color: #1a3a4a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 14 32;");
        }
    }
}