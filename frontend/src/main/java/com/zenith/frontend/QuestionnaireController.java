package com.zenith.frontend;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class QuestionnaireController {

    @FXML private Button backButton;
    @FXML private Button startButton;

    @FXML
    private void handleBack() {
        Parent root = backButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/dashboard.fxml", root, -60);
    }

    @FXML
    private void handleStart() {
        Parent root = startButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/question.fxml", root, 60);
    }
}
