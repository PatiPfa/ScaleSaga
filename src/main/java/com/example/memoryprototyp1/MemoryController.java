package com.example.memoryprototyp1;

import com.example.memoryprototyp1.GameModi.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.control.Button;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import static com.example.memoryprototyp1.MainMenuController.getGamemode;
import static com.example.memoryprototyp1.Score.*;

public class MemoryController implements Initializable {
    @FXML
    private AnchorPane highscoreAnchorPane;
    @FXML
    private AnchorPane popUp;
    @FXML
    private AnchorPane popUp2;
    @FXML
    private FlowPane imagesFlowPane;
    @FXML
    private ImageView displayImageView;
    @FXML
    private ImageView iv_lastcardp1;
    @FXML
    private ImageView iv_lastcardp2;
    @FXML
    private ImageView iv_player1symbol;
    @FXML
    private ImageView iv_player2symbol;
    @FXML
    private Text sec;
    @FXML
    private Text min;
    @FXML
    private Text name2;
    @FXML
    private Text name;
    @FXML
    private Label player1PointsLabel;
    @FXML
    private Label player2PointsLabel;
    @FXML
    private Label playerOnTurnLabel;
    @FXML
    private Label player1name;
    @FXML
    private Label player2name;
    @FXML
    private Label placeFive;
    @FXML
    private Label placeFour;
    @FXML
    private Label placeOne;
    @FXML
    private Label placeThree;
    @FXML
    private Label placeTwo;
    @FXML
    private Label yourScoreLabel;
    @FXML
    private Label errorNameTooLong;
    @FXML
    private TextField highscoreName;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private BaseGame game;
    private Timeline timeline;
    private static boolean timelineIsStopped;
    private boolean alreadyEnteredName; //verhindert mehrfacheingabe im ScoreBoard
    private static String highscoreNameS;
    private static int seconds = 0;
    private static int minutes = 0;

    private Image curser = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/sword.png")));


    public static int getSeconds() {
        return seconds;
    }
    public static int getMinutes() {
        return minutes;
    }

    public static boolean isTimelineIsStopped() {
        return timelineIsStopped;
    }

    /**
     * Methode wird am Start des Spiels automatisch ausgeführt
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Je nachdem welcher Modi gewählt wurde, wird das entsprechende Spiel erstellt
        switch (getGamemode()) {
            case "Singleplayer2Cards":
                this.game = new Singleplayer_2Cards(imagesFlowPane.getChildren().size(), imagesFlowPane, displayImageView, highscoreName, placeFive, placeFour, placeOne, placeThree, placeTwo, highscoreAnchorPane, yourScoreLabel);
                break;
            case "Singleplayer3Cards":
                this.game = new Singleplayer_3Cards(imagesFlowPane.getChildren().size(), imagesFlowPane, displayImageView, highscoreName, placeFive, placeFour, placeOne, placeThree, placeTwo, highscoreAnchorPane, yourScoreLabel);
                break;
            case "Multiplayer2Cards":
                this.game = new MultiplayerForTwo_2Cards(imagesFlowPane.getChildren().size(), imagesFlowPane, player1PointsLabel, player2PointsLabel, playerOnTurnLabel, player1name, player2name, iv_lastcardp1, iv_lastcardp2, popUp, name, iv_player1symbol, iv_player2symbol);
                break;
            case "Multiplayer3Cards":
                this.game = new MultiplayerForTwo_3Cards(imagesFlowPane.getChildren().size(), imagesFlowPane, player1PointsLabel, player2PointsLabel, playerOnTurnLabel, player1name, player2name, popUp2, name2, iv_player1symbol, iv_player2symbol);
                break;
        }

        game.playTheGame();

        //im Singleplayermodus wird der Timer gestartet
        if (getGamemode().equals("Singleplayer2Cards") || getGamemode().equals("Singleplayer3Cards")) {
            timer();
        }
    }

    /**
     * Timer
     **/
    private void timer() {
        timelineIsStopped = false;
        if (timeline != null) {
            timeline.stop();
        }
        seconds = 0;
        minutes = 0;
        sec.setText("0" + String.valueOf(seconds));
        min.setText("0" + String.valueOf(minutes));

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (game.gameFinished()){
                timeline.stop();
            }
            else {
                seconds++;
                if (seconds >= 60) {
                    seconds = 0;
                    minutes++;
                }
            }


            if (seconds < 10){
                sec.setText("0" + String.valueOf(seconds));
            } else {
                sec.setText(String.valueOf(seconds));
            }

            if (minutes < 10){
                min.setText("0" + String.valueOf(minutes));
            }else {
                min.setText(String.valueOf(minutes));
            }


        }));

        timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

    }

    /**
     * Play again Button
     **/
    public void playAgain() {
        //startet Spiel neu
        game.playAgain();
        //im Singleplayer wird der Timer neu gestartet
        if(getGamemode().equals("Singleplayer2Cards") || getGamemode().equals("Singleplayer3Cards")){
            timeline.stop();
            timer();
            alreadyEnteredName = false; //Wichtig für namenseingabe im Scoreboard
        }

    }

    /**
     * Button um zum Hauptmenü zu wechseln
     **/
    public void returnToMainMenu(ActionEvent event) {
        //Im Singleplayer wird der Timer gestoppt
        if(getGamemode().equals("Singleplayer2Cards") || getGamemode().equals("Singleplayer3Cards")){
            timeline.stop();
        }
        //Singleplayer im MainMenuController wird wieder auf false gesetzt um neu auswählen zu können
        MainMenuController.setSingleplayer(false);

        try {
            root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("ScaleSaga!");
            scene = new Scene(root);
            scene.setCursor(new ImageCursor(curser));
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            MainMenuController.writeInLog(e, "Main Menu");
        }
    }

    /**
     * play again Button im PopUp
     **/
    public void playAgainPopUp() {
        popUp.setVisible(false);
        game.playAgain();

    }

    /**
     * play again Button im zweiten PopUp
     **/
    public void playAgainPopUp2(){
        popUp2.setVisible(false);
        game.playAgain();
    }

    /**
     * submit Button im Highscore Board um den Namen zu bestätigen
     **/
    public void submitName() {
        //Input vom Textfeld in eine Variable speichern
        highscoreNameS = highscoreName.getText();

        //länge des Namen überprüfen
        if (highscoreNameS.length() < 3) {
            errorNameTooLong.setText("Name is too short, min 3 characters!");
            return;
        } else if (highscoreNameS.length() > 10) {
            errorNameTooLong.setText("The name is too long, max 10 characters!");
            return;
        }else{
            errorNameTooLong.setText((" "));
        }


        setScoreBoard(deserializeScore());

        Score score = new Score(getMinutes(), getSeconds(), highscoreNameS);

        for (int i = 0; i < 5; i++) {
            if (getScoreBoard()[i] != null) {
                if (score.scoreToNumber() < getScoreBoard()[i].scoreToNumber() && !alreadyEnteredName) {
                    alreadyEnteredName = true;
                    for (int j = 4; j > i; j--) {
                        getScoreBoard()[j] = getScoreBoard()[j - 1];
                    }
                    getScoreBoard()[i] = score;
                    break;
                }
            } else if (!alreadyEnteredName) {
                getScoreBoard()[i] = score;
                alreadyEnteredName = true;
                break;
            }
        }

        serializeScore(getScoreBoard());

        setScoreLabel(placeOne, 0);
        setScoreLabel(placeTwo, 1);
        setScoreLabel(placeThree, 2);
        setScoreLabel(placeFour, 3);
        setScoreLabel(placeFive, 4);
    }


    /**
     * setzt alle Score Label
     **/
    private void setScoreLabel(Label l, int pos) {
        Score[] scores = new Score[5];

        scores[pos] = deserializeScore()[pos];
        if (scores[pos] != null && scores[pos].getScoreSec() < 10) {
            l.setText(scores[pos].getScoreMin() + ":0" + scores[pos].getScoreSec() + " | " + scores[pos].getPlayerName());
        } else if (scores[pos] != null) {
            l.setText(scores[pos].getScoreMin() + ":" + scores[pos].getScoreSec() + " | " + scores[pos].getPlayerName());
        }
    }


    /**
     * schließt das Scoreboard
     **/
    public void returnFromScoreBoard() {
        highscoreAnchorPane.setVisible(false);
    }

    public void timerStop(){
        if (timelineIsStopped){
            timeline.play();
            timelineIsStopped = false;
        } else {
            timeline.stop();
            timelineIsStopped = true;
        }
    }

}

