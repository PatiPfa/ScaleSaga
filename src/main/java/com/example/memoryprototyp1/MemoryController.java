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




    private Timeline timeline;
    int seconds = 0;
    int minutes= 0;
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
            case 1:
                this.game = new Singleplayer_2Cards(imagesFlowPane.getChildren().size(), imagesFlowPane, displayImageView, highscoreName, placeFive, placeFour, placeOne, placeThree, placeTwo, highscoreAnchorPane);
                break;
            case 2:
                this.game = new Singleplayer_3Cards(imagesFlowPane.getChildren().size(), imagesFlowPane);
                break;
            case 3:
                this.game = new MultiplayerForTwo_2Cards(imagesFlowPane.getChildren().size(), imagesFlowPane, player1PointsLabel, player2PointsLabel, playerOnTurnLabel, player1name, player2name, iv_lastcardp1, iv_lastcardp2);
                break;
            case 4:
                this.game = new MultiplayerForTwo_3Cards(imagesFlowPane.getChildren().size(), imagesFlowPane, player1PointsLabel, player2PointsLabel, playerOnTurnLabel, player1name, player2name);
                break;
        }

      game.playTheGame();

        if ( getGamemode() == 1 || getGamemode() == 2) {
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
    }

    public void returnToMainMenu(ActionEvent event) throws IOException {
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

}

