package com.zenith.frontend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
}
