package com.zenith.frontend;

import com.zenith.frontend.api.SessionManager;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalmingGamesController {

    @FXML private HBox navHome, navRelax, navStats, navLogout;
    @FXML private VBox viewBreathing, viewMemory, viewCanvas;
    @FXML private Button btnTabBreathing, btnTabMemory, btnTabCanvas;


    @FXML private Circle breathingCircle;
    @FXML private Label instructionLabel;
    private SequentialTransition breathingAnimation;

    @FXML private GridPane gridMemory;
    private List<String> symbols;
    private List<Button> cardButtons = new ArrayList<>();
    private Button firstCard = null;
    private Button secondCard = null;

    private String firstSymbol = null;
    private String secondSymbol = null;

    private boolean isChecking = false;
    private PauseTransition previewPause;

    @FXML private Canvas drawingCanvas;
    @FXML private ColorPicker colorPicker;
    @FXML private Slider brushSlider;

    @FXML
    public void initialize() {
        setupBreathingAnimation();

        setupCanvas();

        resetMemoryGame();
    }

    @FXML
    private void showBreathingGame() {
        stopPreviewTimer();
        viewBreathing.setVisible(true);
        viewMemory.setVisible(false);
        viewCanvas.setVisible(false);
        setTabStyle(btnTabBreathing, btnTabMemory, btnTabCanvas);
    }

    @FXML
    private void showMemoryGame() {
        handleStop();
        viewBreathing.setVisible(false);
        viewMemory.setVisible(true);
        viewCanvas.setVisible(false);
        setTabStyle(btnTabMemory, btnTabBreathing, btnTabCanvas);
        resetMemoryGame();
    }

    @FXML
    private void showCanvasGame() {
        handleStop();
        stopPreviewTimer();
        viewBreathing.setVisible(false);
        viewMemory.setVisible(false);
        viewCanvas.setVisible(true);
        setTabStyle(btnTabCanvas, btnTabBreathing, btnTabMemory);
    }

    private void setTabStyle(Button active, Button inactive1, Button inactive2) {
        active.setStyle("-fx-background-color: #1a3a4a; -fx-text-fill: white; -fx-background-radius: 25; -fx-padding: 10 24; -fx-font-weight: bold;");
        inactive1.setStyle("-fx-background-color: #d1d5db; -fx-text-fill: #1a3a4a; -fx-background-radius: 25; -fx-padding: 10 24; -fx-font-weight: bold;");
        inactive2.setStyle("-fx-background-color: #d1d5db; -fx-text-fill: #1a3a4a; -fx-background-radius: 25; -fx-padding: 10 24; -fx-font-weight: bold;");
    }

    private void setupBreathingAnimation() {
        ScaleTransition inhale = new ScaleTransition(Duration.seconds(4), breathingCircle);
        inhale.setToX(1.8);
        inhale.setToY(1.8);
        inhale.setOnFinished(e -> instructionLabel.setText("Tahan..."));

        PauseTransition hold = new PauseTransition(Duration.seconds(2));
        hold.setOnFinished(e -> instructionLabel.setText("Hembuskan..."));

        ScaleTransition exhale = new ScaleTransition(Duration.seconds(5), breathingCircle);
        exhale.setToX(1.0);
        exhale.setToY(1.0);
        exhale.setOnFinished(e -> instructionLabel.setText("Tarik Napas..."));

        breathingAnimation = new SequentialTransition(inhale, hold, exhale);
        breathingAnimation.setCycleCount(Animation.INDEFINITE);
    }

    @FXML
    private void handleStart() {
        instructionLabel.setText("Tarik Napas...");
        breathingAnimation.playFromStart();
    }

    @FXML
    private void handleStop() {
        if (breathingAnimation != null) {
            breathingAnimation.stop();
        }
        if (breathingCircle != null) {
            breathingCircle.setScaleX(1.0);
            breathingCircle.setScaleY(1.0);
        }
        if (instructionLabel != null) {
            instructionLabel.setText("Siap");
        }
    }

    @FXML
    private void resetMemoryGame() {
        stopPreviewTimer();
        gridMemory.getChildren().clear();
        cardButtons.clear();
        firstCard = null;
        secondCard = null;
        firstSymbol = null;
        secondSymbol = null;
        isChecking = true;

        symbols = new ArrayList<>(List.of("🌿", "🌿", "🌊", "🌊", "🌸", "🌸", "☀️", "☀️", "☁️", "☁️", "🕊️", "🕊️", "🪷", "🪷", "🍃", "🍃"));
        Collections.shuffle(symbols);

        for (int i = 0; i < symbols.size(); i++) {
            final String symbol = symbols.get(i);
            Button card = new Button(symbol);
            card.setPrefSize(95, 95);
            card.setStyle("-fx-font-size: 28px; -fx-background-color: #e1e9f0; -fx-background-radius: 16; -fx-cursor: hand; -fx-font-weight: bold; -fx-text-fill: #1a3a4a;");

            card.setOnAction(e -> handleCardClick(card, symbol));

            cardButtons.add(card);
            gridMemory.add(card, i % 4, i / 4);
        }

        previewPause = new PauseTransition(Duration.seconds(2.5));
        previewPause.setOnFinished(e -> {
            for (Button card : cardButtons) {
                animateFlip(card, "?");
            }
            isChecking = false;
        });
        previewPause.play();
    }

    private void handleCardClick(Button card, String symbol) {
        if (isChecking || card == firstCard || card.isDisabled()) return;

        animateFlip(card, symbol);

        if (firstCard == null) {
            firstCard = card;
            firstSymbol = symbol;
        } else {
            secondCard = card;
            secondSymbol = symbol;
            isChecking = true;
            checkMatch();
        }
    }

    private void checkMatch() {

        if (firstSymbol != null && firstSymbol.equals(secondSymbol)) {
            firstCard.setStyle("-fx-font-size: 28px; -fx-background-color: #d4edda; -fx-background-radius: 16; -fx-text-fill: #155724; -fx-font-weight: bold;");
            secondCard.setStyle("-fx-font-size: 28px; -fx-background-color: #d4edda; -fx-background-radius: 16; -fx-text-fill: #155724; -fx-font-weight: bold;");

            firstCard.setDisable(true);
            secondCard.setDisable(true);

            clearTurnState();
            isChecking = false;
        } else {
            PauseTransition pause = new PauseTransition(Duration.millis(700));
            pause.setOnFinished(e -> {
                animateFlip(firstCard, "?");
                animateFlip(secondCard, "?");
                clearTurnState();
                isChecking = false;
            });
            pause.play();
        }
    }

    private void clearTurnState() {
        firstCard = null;
        secondCard = null;
        firstSymbol = null;
        secondSymbol = null;
    }

    private void animateFlip(Button card, String newText) {
        if (card == null) return;
        ScaleTransition st1 = new ScaleTransition(Duration.millis(120), card);
        st1.setToX(0);
        st1.setOnFinished(e -> {
            card.setText(newText);
            ScaleTransition st2 = new ScaleTransition(Duration.millis(120), card);
            st2.setToX(1.0);
            st2.play();
        });
        st1.play();
    }

    private void stopPreviewTimer() {
        if (previewPause != null) {
            previewPause.stop();
        }
    }

    private void setupCanvas() {
        GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
        colorPicker.setValue(Color.web("#1a3a4a"));

        drawingCanvas.setOnMousePressed(e -> {
            gc.setStroke(colorPicker.getValue());
            gc.setLineWidth(brushSlider.getValue());
            gc.beginPath();
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });

        drawingCanvas.setOnMouseDragged(e -> {
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });
    }

    @FXML
    private void clearCanvas() {
        drawingCanvas.getGraphicsContext2D().clearRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());
    }

    @FXML
    private void handleHome() {
        handleStop();
        stopPreviewTimer();
        Parent root = navHome.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/dashboard.fxml", root, -60);
    }

    @FXML
    private void handleRelax() {
        handleStop();
        stopPreviewTimer();
        Parent root = navRelax.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/relax_calm.fxml", root, -60);
    }

    @FXML
    private void handleStatistics() {
        handleStop();
        stopPreviewTimer();
        Parent root = navStats.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/history.fxml", root, 60);
    }

    @FXML
    private void handleLogout() {
        handleStop();
        stopPreviewTimer();
        SessionManager.clear();
        Parent root = navLogout.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/login.fxml", root, -60);
    }
}