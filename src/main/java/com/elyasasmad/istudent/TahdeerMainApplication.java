package com.elyasasmad.istudent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class TahdeerMainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // Load custom font
        Font.loadFont(TahdeerMainApplication.class.getResource("fonts/Inter-Regular.ttf").toExternalForm(), 16);
        Font.loadFont(TahdeerMainApplication.class.getResource("fonts/Inter-Bold.ttf").toExternalForm(), 24);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Login | IIUM i-Tahdeer");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}