package com.example.memoryprototyp1;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private static int gamemode = 0;
    //1: Singleplayer 2 Cards, 2: Singleplayer 3 Cards
    //3: Multiplayer 2 Cards, 4: Multiplayer 3 Cards
    public static int getGamemode() {
        return gamemode;
    }

    public void Singleplayer(){
        singleplayer = true;
        btn_singleplayer.setVisible(false);
        btn_multiplayer.setVisible(false);
        btn_2cards.setVisible(true);
        btn_3cards.setVisible(true);
        btn_return.setVisible(true);
    }

    public void Multiplayer(){
        //ToDO: da sollte man die visibility irgendwie abfragen können und dann
        // jeweils das umgekehrte machen und in eine Methode machen und dann nur mehr die Methode aufrufen
        btn_singleplayer.setVisible(false);
        btn_multiplayer.setVisible(false);
        btn_2cards.setVisible(true);
        btn_3cards.setVisible(true);
        btn_return.setVisible(true);
    }

    public void twoCards(ActionEvent event) throws IOException{
        if(singleplayer){
            //ToDO: ev den fxml namen durch ne variable ersetzen und den code in eine eigene funtkion setzen
            //und dann immer die funktion aufrufen und den variablennamen übergeben
            gamemode= 1;
            root = FXMLLoader.load(getClass().getResource("Singleplayer_2Cards.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Singleplayer 2 Cards");
            stage.setScene(scene);
            stage.show();

        }else{
            gamemode = 3;
            root = FXMLLoader.load(getClass().getResource("MultiplayerForTwo_2Cards.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Multiplayer 2 Cards");
            stage.setScene(scene);
            stage.show();
        }
    }

    public void threeCards(ActionEvent event) throws IOException{
        if(singleplayer){
            gamemode = 2;
            root = FXMLLoader.load(getClass().getResource("Singleplayer_3Cards.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Singleplayer 3 Cards");
            stage.setScene(scene);
            stage.show();
        }else{
            gamemode = 4;
            root = FXMLLoader.load(getClass().getResource("MultiplayerForTwo_3Cards.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Multiplayer 3 Cards");
            stage.setScene(scene);
            stage.show();
        }
    }

    public void returnbtn(){
        singleplayer = false;
        btn_singleplayer.setVisible(true);
        btn_multiplayer.setVisible(true);
        btn_2cards.setVisible(false);
        btn_3cards.setVisible(false);
        btn_return.setVisible(false);
    }

}
