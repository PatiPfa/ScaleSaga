package com.example.memoryprototyp1;

import com.example.memoryprototyp1.GameModi.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;
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
    private Label player1PointsLabel= new Label("0");;
    @FXML
    private Label player2PointsLabel= new Label("0");;
    private Text playerpoints = new Text("0");
    private Timeline timeline;
    int seconds = 0;
    int minutes= 0;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Image curser = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/sword.png")));

    public MemoryController() {
        this.player1PointsLabel.setText("0");
        this.player2PointsLabel.setText("0");
        this.playerpoints.setText("0");
    }

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
                MemoryController memoryController = new MemoryController();
                this.game = new MultiplayerForTwo_2Cards(imagesFlowPane.getChildren().size(), imagesFlowPane, memoryController);
                break;

            case 4:
                this.game = new MultiplayerForTwo_3Cards(imagesFlowPane.getChildren().size(), imagesFlowPane);
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
    private void timer() {
        if (timeline != null) {
            timeline.stop();
        }
        seconds = 0;
        minutes = 0;
        sec.setText(String.valueOf(seconds));
        min.setText(String.valueOf(minutes));
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            seconds++;
            if (seconds >= 60) {
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

    public void returnToMainMenu(ActionEvent event) throws IOException {
        MainMenuController.setSingleplayer(false);
        root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("ScaleSaga!");
        stage.setResizable(false);
        stage.setFullScreen(false);
        scene = new Scene(root);
        scene.setCursor(new ImageCursor(curser));
        stage.setScene(scene);
        stage.show();
    }

    public void updateLabels(int p1points, int p2points){
        player1PointsLabel.setText(Integer.toString(p1points));
        player2PointsLabel.setText(Integer.toString(p2points));
        playerpoints.setText(Integer.toString(p1points));
    }

}

