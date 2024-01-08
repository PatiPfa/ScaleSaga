package com.example.memoryprototyp1.GameModi;

import com.example.memoryprototyp1.Card;
import com.example.memoryprototyp1.CardDeck;
import com.example.memoryprototyp1.MemoryCard;
import com.example.memoryprototyp1.Player;
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

import static com.example.memoryprototyp1.Card.getBackOfCards;
import static com.example.memoryprototyp1.Music.playButtonSound;

public class MultiplayerForTwo_2Cards extends BaseGame {

    private Stage stage;
    private Scene scene;
    private Parent root;
    int gameCounter;
    Player player1 = new Player("Player1", Color.RED);
    Player player2 = new Player("Player2", Color.BLUE);
    Player playerOnTurn = player1;
    private MemoryController memoryController;

    private Label player1PointsLabel;
    private Label player2PointsLabel;
    private Label playerOnTurnLabel;

    public MultiplayerForTwo_2Cards(int size, FlowPane imagesFlowPane, Label player1PointsLabel, Label player2PointsLabel, Label playerOnTurnLabel) {
        super(size, imagesFlowPane);
        this.player1PointsLabel = player1PointsLabel;
        this.player2PointsLabel = player2PointsLabel;
        this.playerOnTurnLabel = playerOnTurnLabel;
    }

    @Override
    public void play() {
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

            cardsInGame.add(new MemoryCard(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));
            cardsInGame.add(new MemoryCard(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));

        }
        Collections.shuffle(cardsInGame);
        System.out.println(cardsInGame);
        rotateAllCardsToBackSide();
    }
    @Override
    public void checkForMatch(){
        if (firstCard.sameCardAs(secondCard)){
            System.out.println("same");
            playButtonSound();
            cardsAreFlipped = false;
            firstCard.setCorrectPair(true);
            secondCard.setCorrectPair(true);
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

    private boolean allCardsFlipped() {

        return cardsInGame.stream().allMatch(MemoryCard::isCorrectPair);
    }

    private void updatePointsLabels(){
        player1PointsLabel.setText(Integer.toString(player1.getPoints()));
        player2PointsLabel.setText(Integer.toString(player2.getPoints()));
    }

    private void updatePlayerOnTurnLabel(){
        playerOnTurnLabel.setText(playerOnTurn.getName());
    }
    private void winner(){
        String winner = null;

        if(player1.getPoints() > player2.getPoints()){
            winner = "Player 1";
        }else if(player1.getPoints() < player2.getPoints()){
            winner = "Player 2";
        }else{
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
        }else {
            playerOnTurn = player1;
        }
    }
}
