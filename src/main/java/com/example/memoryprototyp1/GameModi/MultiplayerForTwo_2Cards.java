package com.example.memoryprototyp1.GameModi;

import com.example.memoryprototyp1.*;
import com.example.memoryprototyp1.GameModi.BaseGame;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.memoryprototyp1.Card.getBackOfCards;

public class MultiplayerForTwo_2Cards extends BaseGame {

    private Stage stage;
    private Scene scene;
    private Parent root;
    int gameCounter;
    Player player1 = new Player("Player1", Color.RED);
    Player player2 = new Player("Player2", Color.BLUE);
    Player playerOnTurn = player1;
    private MemoryController memoryController;

    /*
     *  Labelupdate funktioniert noch nicht
     * */
    public MultiplayerForTwo_2Cards(int flowPaneSize, FlowPane imagesFlowPane, MemoryController memoryController) {
        super(flowPaneSize, imagesFlowPane);
        this.memoryController = memoryController;

    }


    @Override
    public void play() {
        updateLabels();

        firstCard = null;
        secondCard = null;
        CardDeck deck = new CardDeck();
        deck.shuffle();
        cardsInGame = new ArrayList<>();
        CardsAreFlipped = false;

        for (int i = 0; i < flowPaneSize / 2; i++) {
            Card topCardFromDeck = deck.giveTopCard();

            cardsInGame.add(new MemoryCard(topCardFromDeck.getName()));
            cardsInGame.add(new MemoryCard(topCardFromDeck.getName()));

        }
        Collections.shuffle(cardsInGame);
        System.out.println(cardsInGame);
        rotateAllCardsToBackSide();
    }
    @Override
    public void checkForMatch(){
        if (firstCard.sameCardAs(secondCard)){
            System.out.println("same");
            CardsAreFlipped = false;
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

        //updateLabels();

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
            CardsAreFlipped = false;
            this.updateLabels();});

    }

    private boolean allCardsFlipped() {

        return cardsInGame.stream().allMatch(MemoryCard::isCorrectPair);
    }

    private void updateLabels(){
        //memoryController.updateLabels(player1.getPoints(), player2.getPoints());
        Platform.runLater(() -> memoryController.updateLabels(player1.getPoints(), player2.getPoints()));

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
