package com.example.memoryprototyp1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;


import static com.example.memoryprototyp1.CardDeck.getNUMBER_OF_IMAGES;
import java.util.Objects;

public class Main extends Application {
    private Image curser = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/sword.png")));
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.setCursor(new ImageCursor(curser));
        stage.setResizable(false);
        stage.setFullScreen(false);
        Random random = new Random();
        Image gameIcon = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/card" + (random.nextInt(getNUMBER_OF_IMAGES()-1)+1) +".png")));
        stage.getIcons().add(gameIcon);
        stage.setTitle("ScaleSaga!");
        stage.setScene(scene);
        stage.show();
        Media musicMedia = new Media(getClass().getResource("/com/example/memoryprototyp1/sounds/musicBackground.mp3").toExternalForm());
        Music.MusicPlayer.playBackgroundMusic(musicMedia);
    }

    public static void main(String[] args) {
        launch();
    }
}