package org.programmercalculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("programmer-calculator-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Programmer calculator");
        stage.setScene(scene);
        stage.show();
    }
}