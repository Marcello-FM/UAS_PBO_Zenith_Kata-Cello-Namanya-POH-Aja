package com.zenith.frontend;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class QuestionnaireController {

    @FXML private Label progressLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Label questionLabel;
    @FXML private Button option1Button;
    @FXML private Button option2Button;
    @FXML private Button option3Button;
    @FXML private Button option4Button;
    @FXML private Button backButton;

    private int currentQuestionIndex = 0;
    private int totalScore = 0;

    private final String[] questions = {
            "Saya merasa sulit untuk menenangkan diri saat sedang banyak pikiran.",
            "Saya merasa terlalu banyak mengkhawatirkan hal-hal kecil.",
            "Saya merasa sedih, kehilangan semangat, atau tertekan akhir-akhir ini.",
            "Saya mudah merasa kesal, gelisah, atau marah karena hal sepele.",
            "Saya merasa sulit untuk fokus pada aktivitas sehari-hari."
    };

    @FXML
    private void initialize() {
        loadQuestion();
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.length) {
            progressLabel.setText((currentQuestionIndex + 1) + " / " + questions.length);
            progressBar.setProgress((double) (currentQuestionIndex + 1) / questions.length);
            questionLabel.setText(questions[currentQuestionIndex]);
        } else {
            showResult();
        }
    }

    private void processAnswer(int score) {
        totalScore += score;
        currentQuestionIndex++;
        loadQuestion();
    }

    @FXML
    private void handleOption1() { processAnswer(0); }

    @FXML
    private void handleOption2() { processAnswer(1); }

    @FXML
    private void handleOption3() { processAnswer(2); }

    @FXML
    private void handleOption4() { processAnswer(3); }

    private void showResult() {
        progressLabel.setText("Selesai");
        progressBar.setProgress(1.0);

        String resultText;
        if (totalScore <= 4) {
            resultText = "Tingkat stres rendah. Pertahankan gaya hidup sehat dan pola pikir positifmu!";
        } else if (totalScore <= 9) {
            resultText = "Tingkat stres sedang. Luangkan waktu untuk istirahat dan lakukan relaksasi.";
        } else {
            resultText = "Tingkat stres tinggi. Sangat disarankan untuk beristirahat total atau konsultasi profesional.";
        }

        questionLabel.setText("Total Skor: " + totalScore + "\n\n" + resultText);

        option1Button.setVisible(false);
        option1Button.setManaged(false);
        option2Button.setVisible(false);
        option2Button.setManaged(false);
        option3Button.setVisible(false);
        option3Button.setManaged(false);
        option4Button.setVisible(false);
        option4Button.setManaged(false);

        backButton.setText("Kembali ke Dashboard");
        backButton.getStyleClass().remove("logout-button");
        backButton.getStyleClass().add("menu-button");
        backButton.setPrefHeight(50);
        backButton.setPrefWidth(350);
    }

    @FXML
    private void handleBack() {
        Parent root = backButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/dashboard.fxml", root, -60);
    }
}