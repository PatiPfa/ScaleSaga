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


    public void Singleplayer(){
        singleplayer = true;
        btn_singleplayer.setVisible(false);
        btn_multiplayer.setVisible(false);
        btn_2cards.setVisible(true);
        btn_3cards.setVisible(true);
        btn_return.setVisible(true);
    }

    public void Multiplayer(){
        btn_singleplayer.setVisible(false);
        btn_multiplayer.setVisible(false);
        btn_2cards.setVisible(true);
        btn_3cards.setVisible(true);
        btn_return.setVisible(true);
    }

    public void twoCards(ActionEvent event) throws IOException{
        if(singleplayer){
            root = FXMLLoader.load(getClass().getResource("Singleplayer_2Cards.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Singleplayer 2 Cards");
            stage.setScene(scene);
            stage.show();
        }else{
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
            root = FXMLLoader.load(getClass().getResource("Singleplayer_3Cards.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Singleplayer 3 Cards");
            stage.setScene(scene);
            stage.show();
        }else{
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
