package com.zenith.frontend;

import com.zenith.frontend.api.AlertHelper;
import com.zenith.frontend.api.ApiClient;
import com.zenith.frontend.api.ApiException;
import com.zenith.frontend.api.ProgressSummaryResponse;
import com.zenith.frontend.api.SessionManager;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DashboardController {

    @FXML private HBox navLogout;

    @FXML private VBox cardStressCheck;
    @FXML private VBox cardMyProgress;
    @FXML private VBox cardRelax;
    @FXML private VBox cardGames;

    @FXML private Label greetingLabel;
    @FXML private Label streakLabel;
    @FXML private Label moodLabel;
    @FXML private Label sessionsLabel;

    private static final String DEFAULT_STRESS = "-fx-background-color: #c8e6f5; -fx-background-radius: 20; -fx-padding: 20; -fx-cursor: hand;";
    private static final String HOVER_STRESS   = "-fx-background-color: #7dd8f0; -fx-background-radius: 20; -fx-padding: 20; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(125,216,240,0.4), 15, 0, 0, 6);";

    private static final String DEFAULT_PROGRESS = "-fx-background-color: #d8c8f0; -fx-background-radius: 20; -fx-padding: 20; -fx-cursor: hand;";
    private static final String HOVER_PROGRESS   = "-fx-background-color: #bfa3f0; -fx-background-radius: 20; -fx-padding: 20; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(191,163,240,0.4), 15, 0, 0, 6);";

    private static final String DEFAULT_RELAX = "-fx-background-color: #f5f0a0; -fx-background-radius: 20; -fx-padding: 20; -fx-cursor: hand;";
    private static final String HOVER_RELAX   = "-fx-background-color: #f5c842; -fx-background-radius: 20; -fx-padding: 20; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(245,200,66,0.4), 15, 0, 0, 6);";

    private static final String DEFAULT_GAMES = "-fx-background-color: #90fe90; -fx-background-radius: 20; -fx-padding: 20; -fx-cursor: hand;";
    private static final String HOVER_GAMES   = "-fx-background-color: #60e060; -fx-background-radius: 20; -fx-padding: 20; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(96,224,96,0.4), 15, 0, 0, 6);";

    @FXML
    private void initialize() {
        String name = SessionManager.getFullName();
        if (name != null && !name.isBlank()) {
            greetingLabel.setText("Hello, " + name.split(" ")[0] + " 👋");
        }

        loadProgress();

        Platform.runLater(() -> {
            setupCardInteraction(cardStressCheck, DEFAULT_STRESS, HOVER_STRESS);
            setupCardInteraction(cardMyProgress, DEFAULT_PROGRESS, HOVER_PROGRESS);
            setupCardInteraction(cardRelax, DEFAULT_RELAX, HOVER_RELAX);
            setupCardInteraction(cardGames, DEFAULT_GAMES, HOVER_GAMES);
        });
    }

    /**
     * Mengatur logika animasi melayang naik ke atas dan perubahan warna kartu secara dinamis
     */
    private void setupCardInteraction(VBox card, String defaultStyle, String hoverStyle) {
        if (card == null) return;

        TranslateTransition tt = new TranslateTransition(Duration.millis(120), card);

        card.setOnMouseEntered(e -> {
            tt.stop();
            card.setStyle(hoverStyle);
            tt.setToY(-6);
            card.setViewOrder(-1.0);
            tt.play();
        });

        card.setOnMouseExited(e -> {
            tt.stop();
            card.setStyle(defaultStyle);
            tt.setToY(0);
            card.setViewOrder(0.0);
            tt.play();
        });
    }

    private void loadProgress() {
        new Thread(() -> {
            try {
                ProgressSummaryResponse progress = ApiClient.getProgress();
                Platform.runLater(() -> applyProgress(progress));
            } catch (ApiException ex) {
                Platform.runLater(() -> AlertHelper.showError(ex.getMessage()));
            }
        }).start();
    }

    private void applyProgress(ProgressSummaryResponse progress) {
        streakLabel.setText(String.valueOf(progress.getDayStreak()));
        moodLabel.setText(progress.getAverageMood());
        sessionsLabel.setText(String.valueOf(progress.getTotalAssessments()));
    }

    @FXML
    private void handleLogout() {
        SessionManager.clear();
        Parent root = navLogout.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/login.fxml", root, -60);
    }

    @FXML
    private void handleStressCheck() {
        navigate("/questionnaire.fxml", 60);
    }

    @FXML
    private void handleMyProgress() {
        navigate("/history.fxml", 60);
    }

    @FXML
    private void handleRelax() {
        navigate("/relax_calm.fxml", 60);
    }

    @FXML
    private void handleCalmingGames() {
        navigate("/calming_games.fxml", 60);
    }

    private void navigate(String fxmlPath, double slideOffset) {
        Parent root = navLogout.getScene().getRoot();
        SceneNavigator.navigateWithAnimation(fxmlPath, root, slideOffset);
    }
}