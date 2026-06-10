package com.zenith.frontend;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ResultController {

    @FXML private Label  resultEmoji;
    @FXML private Label  resultLevel;
    @FXML private Label  resultDesc;
    @FXML private Label  resultScore;
    @FXML private Label  rec1Icon;
    @FXML private Label  rec1Title;
    @FXML private Label  rec1Desc;
    @FXML private Label  rec2Icon;
    @FXML private Label  rec2Title;
    @FXML private Label  rec2Desc;
    @FXML private Button backButton;
    @FXML private Button progressButton;

    @FXML
    private void initialize() {
        int score = ResultData.getInstance().getScore();
        resultScore.setText(String.valueOf(score));

        if (score > 20) {
            applyVeryStress(score);
        } else if (score >= 15) {
            applyModerateStress(score);
        } else {
            applyLowStress(score);
        }
    }

    private void applyLowStress(int score) {
        resultEmoji.setText("😊");
        resultLevel.setText("Low Stress");
        resultLevel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        resultDesc.setText("There is little stress present, you are peaceful.");

        rec1Icon.setText("🎮");
        rec1Title.setText("Play our relaxation games");
        rec1Desc.setText("Try our fun yet relaxing games");

        rec2Icon.setText("📊");
        rec2Title.setText("Track your progress");
        rec2Desc.setText("Regular assessments help identify patterns");
    }

    private void applyModerateStress(int score) {
        resultEmoji.setText("😐");
        resultLevel.setText("Moderate Stress");
        resultLevel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #e65100;");
        resultDesc.setText("You have a moderate level of stress. Take some time to relax and recharge.");

        rec1Icon.setText("🧘");
        rec1Title.setText("Try a relaxation session");
        rec1Desc.setText("Meditation and breathing can ease tension");

        rec2Icon.setText("📝");
        rec2Title.setText("Do another check-in soon");
        rec2Desc.setText("Monitor how your stress changes over time");
    }

    private void applyVeryStress(int score) {
        resultEmoji.setText("😢");
        resultLevel.setText("Very Stress");
        resultLevel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #c62828;");
        resultDesc.setText("Your stress level is high. We recommend speaking with a wellness expert.");

        rec1Icon.setText("👨‍⚕️");
        rec1Title.setText("Talk to an expert");
        rec1Desc.setText("Connect with a psychologist or wellness expert");

        rec2Icon.setText("🧘");
        rec2Title.setText("Start a calming session now");
        rec2Desc.setText("Breathing exercises can provide immediate relief");
    }

    @FXML
    private void handleBack() {
        Parent root = backButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/dashboard.fxml", root, -60);
    }

    @FXML
    private void handleViewProgress() {
        Parent root = progressButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/dashboard.fxml", root, 60);
    }
}
