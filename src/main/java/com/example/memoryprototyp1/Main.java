package com.example.memoryprototyp1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import static com.example.memoryprototyp1.CardDeck.getNUMBER_OF_IMAGES;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setFullScreen(false);
        Random random = new Random();
        Image gameIcon = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/card" + (random.nextInt(getNUMBER_OF_IMAGES()-1)+1) +".png")));
        stage.getIcons().add(gameIcon);
        stage.setTitle("Prototyp!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}