package com.tristanbrewee.tests.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DrawWindowTestMain extends Application {

    private static final String FXML_PATH = "../../javafx/fxmls/DrawWindow.fxml";

    public void start (Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_PATH));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("DrawTest");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
