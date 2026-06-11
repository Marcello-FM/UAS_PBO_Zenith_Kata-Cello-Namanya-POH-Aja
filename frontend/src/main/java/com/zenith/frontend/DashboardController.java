package com.zenith.frontend;

import com.zenith.frontend.api.AlertHelper;
import com.zenith.frontend.api.ApiClient;
import com.zenith.frontend.api.ApiException;
import com.zenith.frontend.api.ProgressSummaryResponse;
import com.zenith.frontend.api.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

    @FXML
    private void initialize() {
        String name = SessionManager.getFullName();
        if (name != null && !name.isBlank()) {
            greetingLabel.setText("Hello, " + name.split(" ")[0] + " 👋");
        }

        loadProgress();
    }

    private void loadProgress() {
        new Thread(() -> {
            try {
                ProgressSummaryResponse progress = ApiClient.getProgress();
                javafx.application.Platform.runLater(() -> applyProgress(progress));
            } catch (ApiException ex) {
                javafx.application.Platform.runLater(() -> AlertHelper.showError(ex.getMessage()));
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
