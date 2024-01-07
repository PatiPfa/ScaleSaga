package com.example.memoryprototyp1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.setTitle("Prototyp!");
        stage.setScene(scene);
        stage.show();
        Media musicMedia = new Media(getClass().getResource("/com/example/memoryprototyp1/sounds/musicBackground.mp3").toExternalForm());
        Music.MusicPlayer.playBackgroundMusic(musicMedia);

    }

    public static void main(String[] args) {
        launch();
    }

}