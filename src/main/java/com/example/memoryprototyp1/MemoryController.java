package com.example.memoryprototyp1;

import com.example.memoryprototyp1.GameModi.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
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
    private FlowPane imagesFlowPane;
    private BaseGame game;
    @FXML
    private Text sec;
    @FXML
    private Text min;
    @FXML
    private ImageView displayImageView;
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
    private ImageView iv_lastcardp1;
    @FXML
    private ImageView iv_lastcardp2;
    @FXML
    private AnchorPane highscoreAnchorPane;
    @FXML
    private TextField highscoreName;

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
    private Button btn_mainMenu_2;
    @FXML
    private Button btn_playAgain2;
    @FXML
    private AnchorPane popUp;
    @FXML
    private ImageView imagePopUp;
    @FXML
    private Label yourScoreLabel;





    private Timeline timeline;
    private static int seconds = 0;
    private static int minutes= 0;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Image curser = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/sword.png")));

    public static int getSeconds() {
        return seconds;
    }

    public static int getMinutes() {
        return minutes;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //1: Singleplayer 2 Cards, 2: Singleplayer 3 Cards
        //3: Multiplayer 2 Cards, 4: Multiplayer 3 Cards
        switch (getGamemode()){
            case "Singleplayer2Cards":
                this.game = new Singleplayer_2Cards(imagesFlowPane.getChildren().size(), imagesFlowPane, displayImageView, highscoreName, placeFive, placeFour, placeOne, placeThree, placeTwo, highscoreAnchorPane, yourScoreLabel);
                break;
            case "Singleplayer3Cards":
                this.game = new Singleplayer_3Cards(imagesFlowPane.getChildren().size(), imagesFlowPane);
                break;
            case "Multiplayer2Cards":
                this.game = new MultiplayerForTwo_2Cards(imagesFlowPane.getChildren().size(), imagesFlowPane, player1PointsLabel, player2PointsLabel, playerOnTurnLabel, player1name, player2name, iv_lastcardp1, iv_lastcardp2, popUp);
                break;
            case "Multiplayer3Cards":
                this.game = new MultiplayerForTwo_3Cards(imagesFlowPane.getChildren().size(), imagesFlowPane, player1PointsLabel, player2PointsLabel, playerOnTurnLabel, player1name, player2name);
                break;
        }

      game.playTheGame();

        if ( getGamemode().equals("Singleplayer2Cards") || getGamemode().equals("Singleplayer3Cards")) {
            timer();
        }else {
            player1PointsLabel.setText("0");
            player2PointsLabel.setText("0");
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
        game.playAgain();
        timer();
        alreadyEnteredName = false;
    }

    public void returnToMainMenu(ActionEvent event){
        MainMenuController.setSingleplayer(false);
        try{
            root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("ScaleSaga!");
            scene = new Scene(root);
            scene.setCursor(new ImageCursor(curser));
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            writeInLog(e, "Main Menu");
        }
    }

    private void writeInLog(Exception e, String Fehlerseite) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("test.log", true))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String timestamp = dateFormat.format(new Date());

            writer.println("["+ timestamp + "] Fehler beim Wechseln zur " + Fehlerseite + "-Seite:");
            e.printStackTrace(writer);
            writer.println();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    public void playAgainPopUp(){
        popUp.setVisible(false);
        game.playAgain();
        timer();


    }

    public void submitName(){
        Score score = new Score(getMinutes(), getSeconds(), highscoreName.getText());

        setScoreBoard(deserializeScore());

        for (int i = 0; i < 5; i++) {
            if (getScoreBoard()[i] != null){
                if (getScoreBoard()[i].getScoreMin() >= score.getScoreMin() && getScoreBoard()[i].getScoreSec() >= score.getScoreSec() && !alreadyEnteredName){
                    alreadyEnteredName = true;
                    for (int j = 4; j > i; j--) {
                        getScoreBoard()[j] = getScoreBoard()[j-1];
                    }
                    getScoreBoard()[i] = score;
                    break;
                }
            } else if (!alreadyEnteredName){
                getScoreBoard()[i] = score;
                alreadyEnteredName = true;
                break;
            }
        }

       serializeScore(getScoreBoard());

        setLabel(placeOne, 0);
        setLabel(placeTwo, 1);
        setLabel(placeThree, 2);
        setLabel(placeFour, 3);
        setLabel(placeFive,4);
    }

    private void setLabel(Label l, int pos){
        Score[] scores = new Score[5];

        scores[pos] = deserializeScore()[pos];
        if (scores[pos] != null){
            l.setText(scores[pos].getScoreMin() + " : " + scores[pos].getScoreSec() + " " + scores[pos].getPlayerName());
        }

    }

}

