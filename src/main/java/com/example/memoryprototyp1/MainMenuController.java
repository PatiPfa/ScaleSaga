package com.example.memoryprototyp1;

import java.io.IOException;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;


public class MainMenuController {

    @FXML
    private Button btn_singleplayer;
    @FXML
    private Button btn_multiplayer;
    @FXML
    private Button btn_2cards;
    @FXML
    private Button btn_3cards;
    @FXML
    private Button btn_return;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private boolean singleplayer = false;
    private Image curser = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/sword.png")));
    private double scaleCurser = 100;
    private static int gamemode = 0;
    //1: Singleplayer 2 Cards, 2: Singleplayer 3 Cards
    //3: Multiplayer 2 Cards, 4: Multiplayer 3 Cards
    public static int getGamemode() {
        return gamemode;
    }

    public void Singleplayer(){
        singleplayer = true;
        btnVisible();
    }

    public void Multiplayer(){
        btnVisible();
    }

    public void btnVisible(){
        btn_singleplayer.setVisible(!btn_singleplayer.isVisible());
        btn_multiplayer.setVisible(!btn_multiplayer.isVisible());
        btn_2cards.setVisible(!btn_2cards.isVisible());
        btn_3cards.setVisible(!btn_3cards.isVisible());
        btn_return.setVisible(!btn_return.isVisible());
    }

    public Stage switchToGame(ActionEvent event, String mode) throws IOException{
        root = FXMLLoader.load(getClass().getResource(mode));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.setCursor(new ImageCursor(curser));
        stage.setScene(scene);
        stage.show();
        return stage;
    }

    public void twoCards(ActionEvent event) throws IOException{
        if(singleplayer){
            gamemode= 1;
            Stage stage = switchToGame(event, "Singleplayer_2Cards.fxml");
            stage.setTitle("Singleplayer 2 Cards");
            stage.show();
            Music.playButtonSound();
        }else{
            gamemode = 3;
            Stage stage = switchToGame(event, "MultiplayerForTwo_2Cards.fxml");
            stage.setTitle("Multiplayer 2 Cards");
            stage.show();
            Music.playButtonSound();
        }
    }

    public void threeCards(ActionEvent event) throws IOException{
        if(singleplayer){
            gamemode = 2;
            Stage stage = switchToGame(event, "Singleplayer_3Cards.fxml");
            stage.setTitle("Singleplayer 3 Cards");
            stage.show();
            Music.playButtonSound();
        }else{
            gamemode = 4;
            Stage stage = switchToGame(event, "MultiplayerForTwo_3Cards.fxml");
            stage.setTitle("Multiplayer 3 Cards");
            stage.show();
            Music.playButtonSound();
        }
    }

    public void returnbtn(){
        singleplayer = false;
        btnVisible();
    }

}
