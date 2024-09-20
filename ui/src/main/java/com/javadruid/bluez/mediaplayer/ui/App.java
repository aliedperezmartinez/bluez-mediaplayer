package com.javadruid.bluez.mediaplayer.ui;

import java.io.Closeable;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private Closeable controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Bluetooth Media Player");
        stage.show();
        controller = loader.getController();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        controller.close();
    }

}
