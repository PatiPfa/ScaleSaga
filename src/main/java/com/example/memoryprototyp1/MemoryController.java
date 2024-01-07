package com.example.memoryprototyp1;

import com.example.memoryprototyp1.GameModi.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static com.example.memoryprototyp1.MainMenuController.getGamemode;

public class MemoryController implements Initializable {

    @FXML
    private FlowPane imagesFlowPane;
    private BaseGame game;
    @FXML
    private Text sec;
    @FXML
    private Text min;
    private Timeline timeline;
    int seconds = 0;
    int minutes= 0;

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
        if ( getGamemode() == 1 || getGamemode() == 2) {
            timer();
        }
    }


//    Timer
    private void timer() {
        if (timeline != null) {
            timeline.stop();
        }
        seconds = 0;
        minutes = 0;
        sec.setText(String.valueOf(seconds));
        min.setText(String.valueOf(minutes));
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (game.gameFinished())
                timeline.stop();
            else
            seconds++;
                if(seconds >= 60) {
                    seconds = 0;
                    minutes++;
                }
            sec.setText(String.valueOf(seconds));
            min.setText(String.valueOf(minutes));
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


    }


    public void playAgain(){
        game.playAgaing();
        timer();
    }
}

