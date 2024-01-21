package com.example.memoryprototyp1.GameModi;

import com.example.memoryprototyp1.Card;
import com.example.memoryprototyp1.CardDeck;
import com.example.memoryprototyp1.Player;
import com.example.memoryprototyp1.*;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import static com.example.memoryprototyp1.Card.getBackOfCardsImage;
import static com.example.memoryprototyp1.Music.MusicPlayer.playButtonSound;

public class MultiplayerForTwo_2Cards extends BaseGame {

    private Stage stage;
    private Scene scene;
    private Parent root;
    Player player1;
    Player player2;
    Player playerOnTurn;
    private Label player1PointsLabel;
    private Label player2PointsLabel;
    private Label playerOnTurnLabel;
    private Label player1name;
    private Label player2name;
    private ImageView iv_lastcardp1;
    private ImageView iv_lastcardp2;
    private int lastClickedCard;
    private AnchorPane popUp;
    private String currentCursor = "sword";
    private final Image CURSOR_SWORD = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/sword.png")));
    private final Image CURSOR_AXE = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/axe.png")));
    //https://www.pngwing.com/de/free-png-tatpt/download?height=114
    private Text name;
    private String CursorPlayer1;
    private String CursorPlayer2;
    private boolean firstRound = true;
    private ImageView iv_player1symbol;
    private ImageView iv_player2symbol;




    private static boolean delayStart = false;

    public MultiplayerForTwo_2Cards(int size, FlowPane imagesFlowPane, Label player1PointsLabel, Label player2PointsLabel, Label playerOnTurnLabel, Label player1name, Label player2name, ImageView iv_lastcardp1, ImageView iv_lastcardp2, AnchorPane popUp, Text name, ImageView iv_player1symbol, ImageView iv_player2symbol) {
        super(size, imagesFlowPane);
        this.player1PointsLabel = player1PointsLabel;
        this.player2PointsLabel = player2PointsLabel;
        this.playerOnTurnLabel = playerOnTurnLabel;
        this.player1name = player1name;
        this.player2name = player2name;
        this.iv_lastcardp1 = iv_lastcardp1;
        this.iv_lastcardp2 = iv_lastcardp2;
        this.popUp = popUp;
        this.name = name;
        this.iv_player1symbol = iv_player1symbol;
        this.iv_player2symbol = iv_player2symbol;
    }

    @Override
    public void initializeImageView() {


        for (int i = 0; i < imagesFlowPane.getChildren().size(); i++) {
            ImageView imageView = (ImageView) imagesFlowPane.getChildren().get(i);
            imageView.setImage(getBackOfCardsImage());
            imageView.setUserData(i);

            imageView.setOnMouseEntered(mouseEnteredEvent ->{
                if (!this.getCardsInGame().get((int) imageView.getUserData()).getRevealed()){
                    this.setImageScale((int) imageView.getUserData(), 1.05);
                }
            });

            imageView.setOnMouseExited(mouseEnteredEvent ->{
                this.setImageScale((int) imageView.getUserData(), 1);
            });

            imageView.setOnMouseClicked(mouseEvent -> {
                if ((delayStart && !this.getCardsInGame().get((int) imageView.getUserData()).getRevealed()) && !this.getCardsAreFlipped()){
                    this.flipCard((int) imageView.getUserData());
                    lastClickedCard = (int) imageView.getUserData();
                }
            });
        }
    }


    @Override
    public void play() {
        delayStart = false;
        PauseTransition initialDelay = new PauseTransition(Duration.seconds(3));
        initialDelay.setOnFinished(event -> {
            delayStart = true;
        });
        initialDelay.play();

        player1 = new Player(MainMenuController.getPlayer1name());
        player2 = new Player(MainMenuController.getPlayer2name());

        Random random = new Random();
        int randomStart = random.nextInt(2) + 1;
        playerOnTurn = (randomStart == 1) ? player1 : player2;

        if(firstRound && randomStart == 1){
            CursorPlayer1 = "sword";
            CursorPlayer2 = "axe";
            firstRound = false;
        }else if (firstRound){
            CursorPlayer1 = "axe";
            CursorPlayer2 = "sword";
            iv_player1symbol.setImage(CURSOR_AXE);
            iv_player2symbol.setImage(CURSOR_SWORD);
            firstRound = false;
        }else if((randomStart == 1 && !currentCursor.equals(CursorPlayer1)) || (randomStart == 1 && !currentCursor.equals(CursorPlayer2))){
            switchCursor();
        }

        player1name.setText(player1.getName());
        player2name.setText(player2.getName());

        if(playerOnTurn.equals(player1)){
            player1name.setTextFill(Color.DARKGREEN);
        }else{
            player2name.setTextFill(Color.DARKGREEN);
        }

        updatePointsLabels();
        updatePlayerOnTurnLabel();

        firstCard = null;
        secondCard = null;
        CardDeck deck = new CardDeck();
        deck.shuffle();
        cardsInGame = new ArrayList<>();
        cardsAreFlipped = false;

        for (int i = 0; i < flowPaneSize / 2; i++) {
            Card topCardFromDeck = deck.giveTopCard();

            cardsInGame.add(new Card(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));
            cardsInGame.add(new Card(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));

        }
        Collections.shuffle(cardsInGame);
        System.out.println(cardsInGame);
        rotateAllCardsToBackSide();
        rotateDisplayImageView(iv_lastcardp1, getBackOfCardsImage());
        rotateDisplayImageView(iv_lastcardp2, getBackOfCardsImage());
    }
    @Override
    public void checkForMatch(){
        if (firstCard.sameCardAs(secondCard)){
            System.out.println("same");
            playButtonSound();
            cardsAreFlipped = false;

            if(playerOnTurn.equals(player1)){
                player1.addOnePoint();
                rotateDisplayImageView(iv_lastcardp1, cardsInGame.get(lastClickedCard).getFrontOfCards());
            }else{
                player2.addOnePoint();
                rotateDisplayImageView(iv_lastcardp2, cardsInGame.get(lastClickedCard).getFrontOfCards());
            }
            updatePlayerOnTurn();
        } else {
            rotateBack();
        }

        firstCard = null;
        secondCard = null;

        updatePointsLabels();

        if(allCardsFlipped()){
            winner();
        }

        updatePlayerOnTurn();
        System.out.println(playerOnTurn.getName());
        System.out.println("Player 1: " + player1.getPoints());
        System.out.println("Player 2: " + player2.getPoints());

        PauseTransition delay = new PauseTransition(Duration.millis(1500));
        delay.play();
        delay.setOnFinished(delayEvent ->{
            cardsAreFlipped = false;
        });

        updatePlayerOnTurnLabel();
    }

    private void updatePointsLabels(){
        player1PointsLabel.setText(Integer.toString(player1.getPoints()));
        player2PointsLabel.setText(Integer.toString(player2.getPoints()));
    }

    private void updatePlayerOnTurnLabel(){
        playerOnTurnLabel.setText(playerOnTurn.getName());
    }
    private void winner(){
        String winner;

        if(player1.getPoints() > player2.getPoints()){
            winner = player1.getName();
            name.setText(winner);
        }else if(player1.getPoints() < player2.getPoints()){
            winner = player2.getName();
            name.setText(winner);
        }else{
            winner = "DRAW";
            name.setText(winner);
        }
        System.out.println(winner);


        popUp.setVisible(!popUp.isVisible());
    }

    public void switchCursor(){
        try{
            Scene scene =  playerOnTurnLabel.getScene();
            if(currentCursor.equals("sword")){
                scene.setCursor(new ImageCursor(CURSOR_AXE));
                currentCursor = "axe";
            } else {
                scene.setCursor(new ImageCursor(CURSOR_SWORD));
                currentCursor = "sword";
            }
        }catch (Exception e){
            MainMenuController.writeInLog(e, "switchCursor");
        }

    }

    public void updatePlayerOnTurn(){
        if(playerOnTurn.equals(player1)){
            playerOnTurn = player2;
            player1name.setTextFill(Color.WHITE);
            player2name.setTextFill(Color.DARKGREEN);
        }else {
            playerOnTurn = player1;
            player1name.setTextFill(Color.DARKGREEN);
            player2name.setTextFill(Color.WHITE);
        }
    }

    public void rotateDisplayImageView(ImageView imageView, Image imageToBeShown) {


        TranslateTransition translate = new TranslateTransition();
        RotateTransition rotateFirstHalf = new RotateTransition();


        translate.setNode(imageView);
        translate.setByY(-10);
        translate.setDuration(Duration.millis(200));

        translate.play();

        translate.setOnFinished(eventTranslateBack -> {
            TranslateTransition translateBack = new TranslateTransition();
            translateBack.setNode(imageView);
            translateBack.setDuration(Duration.millis(100));
            translateBack.setByY(10);
            translateBack.play();
        });

        rotateFirstHalf.setNode(imageView);
        rotateFirstHalf.setDuration(Duration.millis(200));
        rotateFirstHalf.setByAngle(90);
        rotateFirstHalf.setAxis(Rotate.Y_AXIS);
        rotateFirstHalf.play();
        rotateFirstHalf.setOnFinished(eventRotateSecondHalf -> {
            RotateTransition rotateSecondHalf = new RotateTransition();
            rotateSecondHalf.setNode(imageView);
            imageView.setImage(imageToBeShown);
            rotateSecondHalf.setDelay(Duration.seconds(0));
            rotateSecondHalf.setDuration(Duration.millis(200));
            rotateSecondHalf.setByAngle(-90);
            rotateSecondHalf.play();
        });
    }


}
