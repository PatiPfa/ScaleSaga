package com.example.memoryprototyp1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Singleplayer_2Cards_Controller implements Initializable {

    @FXML
    private FlowPane imagesFlowPane;
    private BaseGame game;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**
         * Wenn man das BaseGame spielen möchte, ruft man den Konstruktor BaseGame auf und setzt this.game = baseGame.
         * Wenn man z.B MultiplayerForTwo spielen möchte, ruft man den Konstruktor MultiplayerForTwo auf und setzt this.game = multiplyerForTwo.
         * Im Menü muss noch ein Mechanismus geschaffen werden der im MemoryController den richtigen Konstruktor als this.game setzt und das passende FXML lädt.
         */

//      BaseGame baseGame = new BaseGame(imagesFlowPane.getChildren().size(), imagesFlowPane);
//      this.game = baseGame;

        /**
         * In der Klasse MultiplayerForTwo habe ich Testweise die flipCard Methode überschrieben. Wenn man dieses Spiel lädt kann mann all Karten sofort aufdecken,
         * nicht wie im BaseGame wo man nur zwei gleichzeitig aufdecken kann.
         */
        Singleplayer_2Cards singleplayer_2Cards = new Singleplayer_2Cards(imagesFlowPane.getChildren().size(), imagesFlowPane);
        this.game = singleplayer_2Cards;

      game.playTheGame();
    }


    public void playAgain(){
        game.playAgaing();
    }
}

