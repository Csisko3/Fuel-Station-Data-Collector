package org.example.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main_UI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        AnchorPane full_Pane = FXMLLoader.load(Objects.requireNonNull(Main_UI.class.getResource("hello-view.fxml")));
        stage.setTitle("Charging Station App");
        stage.setScene(new Scene(full_Pane));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}