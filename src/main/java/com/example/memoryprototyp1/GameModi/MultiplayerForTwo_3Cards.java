package com.example.memoryprototyp1.GameModi;

import com.example.memoryprototyp1.*;
import javafx.animation.PauseTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.example.memoryprototyp1.Card.getBackOfCards;
import static com.example.memoryprototyp1.Music.playButtonSound;

public class MultiplayerForTwo_3Cards extends BaseGame {

    private MemoryCard thirdCard;
    private Stage stage;
    private Scene scene;
    private Parent root;
    Player player1;
    Player player2;
    Player playerOnTurn;
    private MemoryController memoryController;

    private Label player1PointsLabel;
    private Label player2PointsLabel;
    private Label playerOnTurnLabel;
    private Label player1name;
    private Label player2name;
    public MultiplayerForTwo_3Cards(int flowPaneSize, FlowPane imagesFlowPane, Label player1PointsLabel, Label player2PointsLabel, Label playerOnTurnLabel, Label player1name, Label player2name) {
        super(flowPaneSize, imagesFlowPane);
        this.player1PointsLabel = player1PointsLabel;
        this.player2PointsLabel = player2PointsLabel;
        this.playerOnTurnLabel = playerOnTurnLabel;
        this.player1name = player1name;
        this.player2name = player2name;
    }
    @Override
    public void play(){
        player1 = new Player(MainMenuController.getPlayer1name());
        player2 = new Player(MainMenuController.getPlayer2name());
//      ToDO: soll ein zufälliger Spieler beginnen???
        playerOnTurn = player1;

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

            cardsInGame.add(new MemoryCard(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));
            cardsInGame.add(new MemoryCard(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));
            cardsInGame.add(new MemoryCard(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));

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

            firstCard.setCorrectPair(true);
            secondCard.setCorrectPair(true);
            thirdCard.setCorrectPair(true);

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

    }

    public void rotateBack(){

        int indexFirstCard = cardsInGame.indexOf(firstCard);
        int indexSecondCard = cardsInGame.indexOf(secondCard);
        int indexThirdCard = cardsInGame.indexOf(thirdCard);
        PauseTransition delay = new PauseTransition(Duration.millis(1500)); //<- time how long the cards are revealed
        delay.play();
        delay.setOnFinished(delayEvent ->{
            rotate(indexFirstCard, getBackOfCards(), 0);
            rotate(indexSecondCard, getBackOfCards(), 0);
            rotate(indexThirdCard, getBackOfCards(), 0);
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
            winner = "Player 1";
        } else if (player1.getPoints() < player2.getPoints()) {
            winner = "Player 2";
        } else {
            winner = "draw";
        }
        System.out.println(winner);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Winner!");
        alert.setHeaderText(null);
        alert.setContentText("Winner: " + winner);
        alert.showAndWait();
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


