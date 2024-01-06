package com.example.memoryprototyp1;

import com.example.memoryprototyp1.GameModi.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.memoryprototyp1.MainMenuController.getGamemode;

public class MemoryController implements Initializable {

    @FXML
    private FlowPane imagesFlowPane;
    private BaseGame game;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //1: Singleplayer 2 Cards, 2: Singleplayer 3 Cards
        //3: Multiplayer 2 Cards, 4: Multiplayer 3 Cards
        switch (getGamemode()){
            case 1:
                this.game = new Singleplayer_2Cards(imagesFlowPane.getChildren().size(), imagesFlowPane);
                break;
            case 2:
                this.game = new Singleplayer_3Cards(imagesFlowPane.getChildren().size(), imagesFlowPane);
                break;
            case 3:
                this.game = new MultiplayerForTwo_2Cards(imagesFlowPane.getChildren().size(), imagesFlowPane);
                break;
            case 4:
                this.game = new MultiplayerForTwo_3Cards(imagesFlowPane.getChildren().size(), imagesFlowPane);
                break;
        }

      game.playTheGame();
    }


    public void playAgain(){
        game.playAgaing();
    }
}

