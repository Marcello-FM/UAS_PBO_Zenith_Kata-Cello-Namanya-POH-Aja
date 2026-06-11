package com.zenith.frontend;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class CalmingGamesController {

    @FXML private HBox navHome;
    @FXML private HBox navRelax;
    @FXML private HBox navLogout;
    @FXML private Circle breathingCircle;
    @FXML private Label instructionLabel;

    private SequentialTransition breathingAnimation;

    @FXML
    private void initialize() {
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
        breathingAnimation.stop();
        breathingCircle.setScaleX(1.0);
        breathingCircle.setScaleY(1.0);
        instructionLabel.setText("Siap");
    }

    @FXML
    private void handleHome() {
        handleStop();
        Parent root = navHome.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/dashboard.fxml", root, -60);
    }

    @FXML
    private void handleRelax() {
        handleStop();
        Parent root = navHome.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/relax_calm.fxml", root, -60);
    }

    @FXML
    private void handleLogout() {
        handleStop();
        Parent root = navLogout.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/login.fxml", root, -60);
    }
}