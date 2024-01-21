package com.example.memoryprototyp1.GameModi;

import com.example.memoryprototyp1.*;
import com.example.memoryprototyp1.GameModi.BaseGame;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.memoryprototyp1.Card.getBackOfCardsImage;
import static com.example.memoryprototyp1.Music.MusicPlayer.playButtonSound;
import static com.example.memoryprototyp1.Score.deserializeScore;


public class Singleplayer_3Cards extends BaseGame {

    private Card thirdCard;
    private ImageView displayImageView;
    private TextField highscoreName;
    private Label placeFive;
    private Label placeFour;

    private Label placeOne;

    private Label placeThree;

    private Label placeTwo;

    private Label yourScoreLabel;
    private AnchorPane highscoreAnchorPane;
    public Singleplayer_3Cards(int flowPaneSize, FlowPane imagesFlowPane, ImageView displayImageView, TextField highscoreName, Label placeFive, Label placeFour, Label placeOne, Label placeThree, Label placeTwo, AnchorPane highscoreAnchorPane, Label yourScoreLabel)
    {
        super(flowPaneSize, imagesFlowPane);
        this.displayImageView = displayImageView;
        this.highscoreName = highscoreName;
        this.placeFive = placeFive;
        this.placeFour = placeFour;
        this.placeOne = placeOne;
        this.placeThree = placeThree;
        this.placeTwo = placeTwo;
        this.highscoreAnchorPane = highscoreAnchorPane;
        this.yourScoreLabel = yourScoreLabel;
    }


    @Override
    public void play(){
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
    public void flipCard(int cardPosition){

        cardsInGame.get(cardPosition).setRevealed(true);

        if (firstCard == null){
            firstCard = cardsInGame.get(cardPosition);
            rotate(cardPosition, cardsInGame.get(cardPosition).getFrontOfCards(), 0);

        } else if (secondCard == null){
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


        } else {
          rotateBack();
        }

        if (gameFinished()) {
            highscoreAnchorPane.setVisible(true);
            if (MemoryController.getSeconds() < 10){
                yourScoreLabel.setText("YOUR TIME: " + MemoryController.getMinutes() + ":0" + MemoryController.getSeconds());
            } else {
                yourScoreLabel.setText("YOUR TIME: " + MemoryController.getMinutes() + ":" + MemoryController.getSeconds());
            }

            Score.setScoreBoard(Score.deserializeScore());
            setScoreLabel(placeOne, 0);
            setScoreLabel(placeTwo, 1);
            setScoreLabel(placeThree, 2);
            setScoreLabel(placeFour, 3);
            setScoreLabel(placeFive,4);
        }

        firstCard = null;
        secondCard = null;
        thirdCard = null;

        PauseTransition delay = new PauseTransition(Duration.millis(1500));
        if (cardsAreFlipped){
            delay.play();
        }
        delay.setOnFinished(delayEvent ->{
            cardsAreFlipped = false;});
    }



    @Override
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
    private void setScoreLabel(Label l, int pos) {
        Score[] scores = new Score[5];

        scores[pos] = deserializeScore()[pos];
        if (scores[pos] != null && scores[pos].getScoreSec() < 10) {
            l.setText(scores[pos].getScoreMin() + ":0" + scores[pos].getScoreSec() + " | " + scores[pos].getPlayerName());
        } else if (scores[pos] != null) {
            l.setText(scores[pos].getScoreMin() + ":" + scores[pos].getScoreSec() + " | " + scores[pos].getPlayerName());
        }

    }
}
