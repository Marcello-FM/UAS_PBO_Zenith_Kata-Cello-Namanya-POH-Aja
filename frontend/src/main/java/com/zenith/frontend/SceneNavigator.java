package com.zenith.frontend;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneNavigator {

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void navigateTo(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(SceneNavigator.class.getResource(fxmlPath));
            primaryStage.setScene(new Scene(root, 1280, 720));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void navigateWithAnimation(String fxmlPath, Parent currentRoot) {
        navigateWithAnimation(fxmlPath, currentRoot, 60);
    }

    public static void navigateWithAnimation(String fxmlPath, Parent currentRoot, double slideFromX) {

        FadeTransition fadeOut = new FadeTransition(Duration.millis(220), currentRoot);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setInterpolator(Interpolator.EASE_IN);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(220), currentRoot);
        scaleOut.setFromX(1.0); scaleOut.setFromY(1.0);
        double scaleTarget = slideFromX > 0 ? 1.03 : 0.97;
        scaleOut.setToX(scaleTarget); scaleOut.setToY(scaleTarget);
        scaleOut.setInterpolator(Interpolator.EASE_IN);

        ParallelTransition phaseA = new ParallelTransition(fadeOut, scaleOut);

        final Parent[] newRootHolder = new Parent[1];
        Thread loader = new Thread(() -> {
            try {
                Parent loaded = FXMLLoader.load(SceneNavigator.class.getResource(fxmlPath));
                loaded.setOpacity(0.0);
                loaded.setTranslateX(slideFromX);
                newRootHolder[0] = loaded;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        loader.setDaemon(true);
        loader.start();

        phaseA.setOnFinished(e -> {
            try { loader.join(500); } catch (InterruptedException ignored) {}

            Parent newRoot = newRootHolder[0];
            if (newRoot == null) return;

            Scene newScene = new Scene(newRoot, 1280, 720);
            newScene.setFill(javafx.scene.paint.Color.web("#1a3a4a"));

            Platform.runLater(() -> {
                primaryStage.setScene(newScene);
                primaryStage.show();

                FadeTransition fadeIn = new FadeTransition(Duration.millis(320), newRoot);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.setInterpolator(Interpolator.EASE_OUT);

                Timeline slideIn = new Timeline(
                        new KeyFrame(Duration.ZERO,
                                new KeyValue(newRoot.translateXProperty(), slideFromX)),
                        new KeyFrame(Duration.millis(320),
                                new KeyValue(newRoot.translateXProperty(), 0.0,
                                        Interpolator.EASE_OUT))
                );

                new ParallelTransition(fadeIn, slideIn).play();
            });
        });

        phaseA.play();
    }
}