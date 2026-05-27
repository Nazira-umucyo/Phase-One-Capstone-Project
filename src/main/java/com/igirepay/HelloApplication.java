package com.igirepay;

import com.igirepay.lab3.ui.MainMenu;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import com.igirepay.lab2.util.DatabaseInitializer;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("IgirePay");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();
        launch(args);
    }
}
