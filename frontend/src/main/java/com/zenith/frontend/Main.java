package com.zenith.frontend;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Zenith Mental Health");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}