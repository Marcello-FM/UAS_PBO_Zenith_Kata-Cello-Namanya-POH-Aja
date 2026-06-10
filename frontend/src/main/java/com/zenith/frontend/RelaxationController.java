package com.zenith.frontend;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class RelaxationController {

    @FXML private Button backButton;

    @FXML
    private void handleBack() {
        Parent root = backButton.getScene().getRoot();
        SceneNavigator.navigateWithAnimation("/dashboard.fxml", root, -60);
    }
}