package com.zenith.frontend.api;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public final class AlertHelper {

    private AlertHelper() {}

    public static boolean showConfirm(String message) {
        if (Platform.isFxApplicationThread()) {
            return showConfirmOnFxThread(message);
        }

        final boolean[] result = {false};
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            result[0] = showConfirmOnFxThread(message);
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return result[0];
    }

    private static boolean showConfirmOnFxThread(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Zenith");
        alert.setHeaderText(null);
        alert.setContentText(message);

        styleAlert(alert);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public static void showError(String message) {
        runOnFxThread(() -> showAlert(Alert.AlertType.ERROR, message));
    }

    public static void showInfo(String message) {
        runOnFxThread(() -> showAlert(Alert.AlertType.INFORMATION, message));
    }

    private static void runOnFxThread(Runnable action) {
        if (Platform.isFxApplicationThread()) {
            action.run();
        } else {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.runLater(() -> {
                action.run();
                latch.countDown();
            });
            try {
                latch.await();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Zenith");
        alert.setHeaderText(null);
        alert.setContentText(message);

        styleAlert(alert);

        alert.showAndWait();
    }

    private static void styleAlert(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();

        dialogPane.setStyle("-fx-background-color: #1a3a4a; " +
                "-fx-border-color: #142e3e; " +
                "-fx-border-width: 2px;");

        javafx.scene.Node contentText = dialogPane.lookup(".content.label");
        if (contentText != null) {
            contentText.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        }

        javafx.scene.Node headerText = dialogPane.lookup(".header-panel");
        if (headerText != null) {
            headerText.setStyle("-fx-background-color: #142e3e;");
        }

        for (ButtonType buttonType : alert.getButtonTypes()) {
            Button button = (Button) dialogPane.lookupButton(buttonType);
            if (button != null) {
                button.setStyle("-fx-background-color: #87CEFA; " +
                        "-fx-text-fill: #1a3a4a; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 6 20 6 20; " +
                        "-fx-background-radius: 15;");
            }
        }
    }
}