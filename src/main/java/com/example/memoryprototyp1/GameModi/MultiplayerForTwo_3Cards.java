package com.example.memoryprototyp1.GameModi;

import com.example.memoryprototyp1.*;
import javafx.animation.PauseTransition;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import static com.example.memoryprototyp1.Card.getBackOfCardsImage;
import static com.example.memoryprototyp1.Music.MusicPlayer.playButtonSound;

public class MultiplayerForTwo_3Cards extends BaseGame {

    private Card thirdCard;

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

    private AnchorPane popUp2;
    private Text name2;

    private String currentCursor = "sword";
    private final Image CURSOR_SWORD = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/sword.png")));
    private final Image CURSOR_AXE = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/axe.png")));

    private boolean delayStart = false;
    public MultiplayerForTwo_3Cards(int flowPaneSize, FlowPane imagesFlowPane, Label player1PointsLabel, Label player2PointsLabel, Label playerOnTurnLabel, Label player1name, Label player2name, AnchorPane popUp2, Text name2) {
        super(flowPaneSize, imagesFlowPane);
        this.player1PointsLabel = player1PointsLabel;
        this.player2PointsLabel = player2PointsLabel;
        this.playerOnTurnLabel = playerOnTurnLabel;
        this.player1name = player1name;
        this.player2name = player2name;
        this.popUp2 = popUp2;
        this.name2 = name2;
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
                if ((delayStart && !this.getCardsInGame().get((int) imageView.getUserData()).getRevealed()) && !cardsAreFlipped){
                    this.flipCard((int) imageView.getUserData());
                }
            });
        }
    }
    @Override
    public void play(){
        PauseTransition initialDelay = new PauseTransition(Duration.seconds(3));
        initialDelay.setOnFinished(event -> {
            delayStart = true;
        });
        initialDelay.play();

        player1 = new Player(MainMenuController.getPlayer1name());
        player2 = new Player(MainMenuController.getPlayer2name());

        Random random = new Random();
        int randomStart = random.nextInt(2) + 1;
        System.out.println(randomStart);
        playerOnTurn = (randomStart == 1) ? player1 : player2;

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
        thirdCard = null;
        CardDeck deck = new CardDeck();
        deck.shuffle();
        cardsInGame = new ArrayList<>();
        cardsAreFlipped = false;


        for (int i = 0; i < flowPaneSize / 3; i++) {
            Card topCardFromDeck = deck.giveTopCard();

            cardsInGame.add(new Card(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));
            cardsInGame.add(new Card(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));
            cardsInGame.add(new Card(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));

        }
        Collections.shuffle(cardsInGame);
        System.out.println(cardsInGame);
        rotateAllCardsToBackSide();
    }
    @Override
    public void flipCard(int cardPosition) {

        cardsInGame.get(cardPosition).setRevealed(true);

        if (firstCard == null) {
            firstCard = cardsInGame.get(cardPosition);
            rotate(cardPosition, cardsInGame.get(cardPosition).getFrontOfCards(), 0);
        } else if (secondCard == null) {
            secondCard = cardsInGame.get(cardPosition);
            rotate(cardPosition, cardsInGame.get(cardPosition).getFrontOfCards(), 0);
        } else {
            thirdCard = cardsInGame.get(cardPosition);
            rotate(cardPosition, cardsInGame.get(cardPosition).getFrontOfCards(), 0);
            cardsAreFlipped = true;
            checkForMatch();
        }
    }
    @Override
    public void checkForMatch(){

        if (firstCard.sameCardAs(secondCard) && firstCard.sameCardAs(thirdCard)){
            System.out.println("same");
            playButtonSound();
            cardsAreFlipped = false;


            if(playerOnTurn.equals(player1)){
                player1.addOnePoint();
            }else{
                player2.addOnePoint();
            }

            //Ev durch ne Variable ersetzen und unten abfragen?
            updatePlayerOnTurn();
        } else {
            rotateBack();
        }

        firstCard = null;
        secondCard = null;
        thirdCard = null;

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
            cardsAreFlipped = false;});
        updatePlayerOnTurnLabel();
        switchCursor();

    }

    public void rotateBack(){

        int indexFirstCard = cardsInGame.indexOf(firstCard);
        int indexSecondCard = cardsInGame.indexOf(secondCard);
        int indexThirdCard = cardsInGame.indexOf(thirdCard);
        PauseTransition delay = new PauseTransition(Duration.millis(1500)); //<- time how long the cards are revealed
        delay.play();
        delay.setOnFinished(delayEvent ->{
            rotate(indexFirstCard, getBackOfCardsImage(), 0);
            rotate(indexSecondCard, getBackOfCardsImage(), 0);
            rotate(indexThirdCard, getBackOfCardsImage(), 0);
            PauseTransition delay2 = new PauseTransition(Duration.millis(485));//<- after delay setRevealed is set false, this prevents card flip bugs
            delay2.play();
            delay2.setOnFinished(cardsAreFlippedBack ->{
                cardsInGame.get(indexFirstCard).setRevealed(false);
                cardsInGame.get(indexSecondCard).setRevealed(false);
                cardsInGame.get(indexThirdCard).setRevealed(false);
            });
        });
    }

    private void updatePointsLabels(){
        player1PointsLabel.setText(Integer.toString(player1.getPoints()));
        player2PointsLabel.setText(Integer.toString(player2.getPoints()));
    }

    private void updatePlayerOnTurnLabel(){
        playerOnTurnLabel.setText(playerOnTurn.getName());
    }
    private void winner() {
        String winner;

        if (player1.getPoints() > player2.getPoints()) {
            winner = player1.getName();
            name2.setText(winner);
        } else if (player1.getPoints() < player2.getPoints()) {
            winner = player2.getName();
            name2.setText(winner);
        } else {
            winner = "DRAW";
        }
        System.out.println(winner);

        popUp2.setVisible(!popUp2.isVisible());

    }

    public void switchCursor(){
        try{
            Stage stage = (Stage) playerOnTurnLabel.getScene().getWindow();
            Scene scene = stage.getScene();
            if(currentCursor.equals("sword")){
                scene.setCursor(new ImageCursor(CURSOR_AXE));
                currentCursor = "axe";
            }else{
                scene.setCursor(new ImageCursor(CURSOR_SWORD));
                currentCursor = "sword";
            }
        }catch (Exception e){
            e.printStackTrace();
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


}


