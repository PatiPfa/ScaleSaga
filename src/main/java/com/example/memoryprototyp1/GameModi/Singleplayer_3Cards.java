package com.example.memoryprototyp1.GameModi;

import com.example.memoryprototyp1.Card;
import com.example.memoryprototyp1.CardDeck;
import com.example.memoryprototyp1.GameModi.BaseGame;
import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.memoryprototyp1.Card.getBackOfCardsImage;
import static com.example.memoryprototyp1.Music.MusicPlayer.playButtonSound;

public class Singleplayer_3Cards extends BaseGame {

    private Card thirdCard;
    public Singleplayer_3Cards(int flowPaneSize, FlowPane imagesFlowPane) {
        super(flowPaneSize, imagesFlowPane);
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Winner!");
            alert.setHeaderText(null);
            alert.setContentText("You have won :D");
            alert.showAndWait();
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
}
