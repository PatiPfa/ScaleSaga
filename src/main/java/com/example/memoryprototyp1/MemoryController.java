package com.example.memoryprototyp1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.memoryprototyp1.Card.getBackOfCards;

public class MemoryController implements Initializable {

    @FXML
    private FlowPane imagesFlowPane;
    private BaseGame game;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      initializeImageView();

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

        MultiplayerForTwo multiplayerForTwo = new MultiplayerForTwo(imagesFlowPane.getChildren().size(), imagesFlowPane);
      this.game = multiplayerForTwo;

      game.play();
    }


    public void initializeImageView() {

        for (int i = 0; i < imagesFlowPane.getChildren().size(); i++) {
            ImageView imageView = (ImageView) imagesFlowPane.getChildren().get(i);
            imageView.setImage(getBackOfCards());
            imageView.setUserData(i);

            imageView.setOnMouseEntered(mouseEnteredEvent ->{
                if (!game.getCardsInGame().get((int) imageView.getUserData()).getRevealed()){
                    game.setImageScale((int) imageView.getUserData(), 1.05);
                }
            });

            imageView.setOnMouseExited(mouseEnteredEvent ->{
                game.setImageScale((int) imageView.getUserData(), 1);
            });

            imageView.setOnMouseClicked(mouseEvent -> {
                if ((!game.getCardsInGame().get((int) imageView.getUserData()).getRevealed()) && !game.getBothCardsAreFlipped()){
                    game.flipCard((int) imageView.getUserData());
                }

            });
        }
    }

    public void playAgain(){
        game.playAgaing();
    }
}

