package com.zenith.frontend;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class QuestionController {

    @FXML private Label        progressLabel;
    @FXML private Label        progressPercent;
    @FXML private Label        sideProgressLabel;
    @FXML private StackPane    progressTrack;
    @FXML private Region       progressFillRegion;
    @FXML private Label        questionLabel;
    @FXML private VBox         answersBox;
    @FXML private ToggleButton opt0, opt1, opt2, opt3, opt4;
    @FXML private Button       backButton;
    @FXML private Button       nextButton;
    @FXML private Button       exitButton;

    private static final String[] QUESTIONS = {
        "How often have you felt stressed in the past week?",
        "How well have you been sleeping recently?",
        "How often have you felt overwhelmed by your responsibilities?",
        "How often have you experienced physical symptoms of stress (e.g. headaches, tension)?",
        "How would you rate your overall mood over the past week?"
    };

    private static final String[][] OPTIONS = {
        { "Never", "Rarely", "Sometimes", "Often", "Always" },
        { "Very well", "Fairly well", "Okay", "Poorly", "Very poorly" },
        { "Never", "Rarely", "Sometimes", "Often", "Always" },
        { "Never", "Rarely", "Sometimes", "Often", "Always" },
        { "Very good", "Good", "Neutral", "Bad", "Very bad" }
    };

    private int currentIndex = 0;
    private final int[] answers = new int[QUESTIONS.length];

    // ToggleGroup initialized as field — guaranteed non-null before any event fires
    private final ToggleGroup toggleGroup = new ToggleGroup();

    private static final String STYLE_UNSELECTED =
            "-fx-background-color: white;" +
            "-fx-background-radius: 14;" +
            "-fx-font-size: 15px;" +
            "-fx-text-fill: #1a3a4a;" +
            "-fx-alignment: CENTER_LEFT;" +
            "-fx-padding: 0 20 0 20;" +
            "-fx-cursor: hand;" +
            "-fx-border-color: transparent;" +
            "-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.06),6,0,0,2);";

    private static final String STYLE_SELECTED =
            "-fx-background-color: #1a3a4a;" +
            "-fx-background-radius: 14;" +
            "-fx-font-size: 15px;" +
            "-fx-text-fill: white;" +
            "-fx-alignment: CENTER_LEFT;" +
            "-fx-padding: 0 20 0 20;" +
            "-fx-cursor: hand;" +
            "-fx-border-color: transparent;" +
            "-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.10),8,0,0,3);";

    private static final String BTN_NEXT_ACTIVE =
            "-fx-background-color: #1a3a4a;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 14;" +
            "-fx-cursor: hand;" +
            "-fx-border-color: transparent;" +
            "-fx-opacity: 1.0;";

    private static final String BTN_NEXT_DISABLED =
            "-fx-background-color: #546e7a;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 14;" +
            "-fx-cursor: default;" +
            "-fx-border-color: transparent;" +
            "-fx-opacity: 0.45;";

    private static final String BTN_BACK_ACTIVE =
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #1a3a4a;" +
            "-fx-font-size: 15px;" +
            "-fx-cursor: hand;" +
            "-fx-border-color: transparent;";

    private static final String BTN_BACK_DISABLED =
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #c0cfd6;" +
            "-fx-font-size: 15px;" +
            "-fx-cursor: default;" +
            "-fx-border-color: transparent;";

    @FXML
    private void initialize() {
        java.util.Arrays.fill(answers, -1);

        List<ToggleButton> opts = List.of(opt0, opt1, opt2, opt3, opt4);

        for (ToggleButton tb : opts) {
            tb.setToggleGroup(toggleGroup);
        }

        toggleGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            for (ToggleButton tb : opts) {
                tb.setStyle(tb.isSelected() ? STYLE_SELECTED : STYLE_UNSELECTED);
            }
            updateNextButton(newT != null);
        });

        loadQuestion(0);
    }

    private void loadQuestion(int index) {
        currentIndex = index;
        int total  = QUESTIONS.length;
        int num    = index + 1;
        double pct = (num / (double) total) * 100.0;

        progressLabel.setText("Question " + num + " of " + total);
        progressPercent.setText((int) pct + "%");
        sideProgressLabel.setText("Question " + num + " of " + total);

        double pctDecimal = pct / 100.0;
        Runnable applyWidth = () ->
            progressFillRegion.setPrefWidth(progressTrack.getWidth() * pctDecimal);

        if (progressTrack.getWidth() > 0) {
            applyWidth.run();
        } else {
            progressFillRegion.setPrefWidth(840 * pctDecimal); // fallback
        }
        progressTrack.widthProperty().addListener((obs, o, n) ->
            progressFillRegion.setPrefWidth(n.doubleValue() * pctDecimal));

        questionLabel.setText(QUESTIONS[index]);

        List<ToggleButton> opts = List.of(opt0, opt1, opt2, opt3, opt4);
        String[] choices = OPTIONS[index];
        for (int i = 0; i < opts.size(); i++) {
            boolean visible = i < choices.length;
            opts.get(i).setText(visible ? choices[i] : "");
            opts.get(i).setVisible(visible);
            opts.get(i).setManaged(visible);
            opts.get(i).setStyle(STYLE_UNSELECTED);
        }

        toggleGroup.selectToggle(null);
        if (answers[index] >= 0) {
            ToggleButton prev = opts.get(answers[index]);
            prev.setSelected(true);
            prev.setStyle(STYLE_SELECTED);
        }

        boolean canGoBack = index > 0;
        backButton.setDisable(!canGoBack);
        backButton.setStyle(canGoBack ? BTN_BACK_ACTIVE : BTN_BACK_DISABLED);

        updateNextButton(answers[index] >= 0);

        nextButton.setText(index == total - 1 ? "Submit" : "Next  \u2192");
    }

    @FXML
    private void handleOptionSelected() {
        Toggle selected = toggleGroup.getSelectedToggle();
        if (selected == null) return;

        List<ToggleButton> opts = List.of(opt0, opt1, opt2, opt3, opt4);
        for (int i = 0; i < opts.size(); i++) {
            if (opts.get(i) == selected) {
                answers[currentIndex] = i;
                break;
            }
        }
    }

    private void updateNextButton(boolean enabled) {
        nextButton.setDisable(!enabled);
        nextButton.setStyle(enabled ? BTN_NEXT_ACTIVE : BTN_NEXT_DISABLED);
    }

    @FXML
    private void handleBack() {
        if (currentIndex > 0) loadQuestion(currentIndex - 1);
    }

    @FXML
    private void handleNext() {
        if (currentIndex < QUESTIONS.length - 1) {
            loadQuestion(currentIndex + 1);
        } else {
            int score = 0;
            for (int a : answers) score += (a >= 0 ? a + 1 : 0);
            ResultData.getInstance().setScore(score);
            ResultData.getInstance().setAnswers(answers);
            Parent root = nextButton.getScene().getRoot();
            SceneNavigator.navigateWithAnimation("/result.fxml", root, 60);
        }
    }

    @FXML
    private void handleExit() {
        Parent root = exitButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/dashboard.fxml", root, -60);
    }
}
