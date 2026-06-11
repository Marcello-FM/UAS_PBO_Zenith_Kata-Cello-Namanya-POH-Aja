package com.zenith.frontend;

import com.zenith.frontend.api.AlertHelper;
import com.zenith.frontend.api.ApiClient;
import com.zenith.frontend.api.ApiException;
import com.zenith.frontend.api.AssessmentResponse;
import com.zenith.frontend.api.ProgressSummaryResponse;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HistoryController {

    @FXML private Label streakLabel;
    @FXML private Label moodLabel;
    @FXML private Label sessionsLabel;
    @FXML private Label averageScoreLabel;
    @FXML private VBox historyList;
    @FXML private Button backButton;

    @FXML
    private void initialize() {
        loadHistory();
    }

    private void loadHistory() {
        new Thread(() -> {
            try {
                ProgressSummaryResponse progress = ApiClient.getProgress();
                javafx.application.Platform.runLater(() -> renderProgress(progress));
            } catch (ApiException ex) {
                javafx.application.Platform.runLater(() -> AlertHelper.showError(ex.getMessage()));
            }
        }).start();
    }

    private void renderProgress(ProgressSummaryResponse progress) {
        streakLabel.setText(String.valueOf(progress.getDayStreak()));
        moodLabel.setText(progress.getAverageMood());
        sessionsLabel.setText(String.valueOf(progress.getTotalAssessments()));
        averageScoreLabel.setText(String.valueOf(progress.getAverageScore()));

        historyList.getChildren().clear();

        if (progress.getHistory().isEmpty()) {
            Label empty = new Label("Belum ada riwayat assessment. Mulai stress check dari dashboard.");
            empty.setWrapText(true);
            empty.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
            historyList.getChildren().add(empty);
            return;
        }

        for (AssessmentResponse item : progress.getHistory()) {
            historyList.getChildren().add(createHistoryRow(item));
        }
    }

    private HBox createHistoryRow(AssessmentResponse item) {
        Label row = new Label(
                formatDate(item.getCreatedAt()) + "  •  Skor " + item.getScore()
                        + "  •  " + item.getStressLabel());
        row.setWrapText(true);
        row.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        HBox container = new HBox(row);
        container.setStyle(
                "-fx-padding: 10 14; -fx-background-color: #24495e; -fx-background-radius: 12;");
        return container;
    }

    private String formatDate(String createdAt) {
        if (createdAt == null || createdAt.isBlank()) {
            return "-";
        }
        return createdAt.replace('T', ' ').substring(0, Math.min(16, createdAt.length()));
    }

    @FXML
    private void handleBack() {
        Parent root = backButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/dashboard.fxml", root, -60);
    }
}
