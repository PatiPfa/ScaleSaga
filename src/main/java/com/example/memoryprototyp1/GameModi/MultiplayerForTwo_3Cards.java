package com.example.memoryprototyp1.GameModi;

import com.example.memoryprototyp1.Card;
import com.example.memoryprototyp1.CardDeck;
import com.example.memoryprototyp1.GameModi.BaseGame;
import com.example.memoryprototyp1.MemoryCard;
import com.example.memoryprototyp1.Player;
import javafx.animation.PauseTransition;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.memoryprototyp1.Card.getBackOfCards;

public class MultiplayerForTwo_3Cards extends BaseGame {

    private MemoryCard thirdCard;
    public MultiplayerForTwo_3Cards(int flowPaneSize, FlowPane imagesFlowPane) {
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
        CardsAreFlipped = false;


        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        player1.setColor(Color.RED);
        player2.setColor(Color.BLUE);

        for (int i = 0; i < flowPaneSize / 3; i++) {
            Card topCardFromDeck = deck.giveTopCard();

            cardsInGame.add(new MemoryCard(topCardFromDeck.getName()));
            cardsInGame.add(new MemoryCard(topCardFromDeck.getName()));
            cardsInGame.add(new MemoryCard(topCardFromDeck.getName()));


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
            rotate(cardPosition, cardsInGame.get(cardPosition).getImage(), 0);
        } else if (secondCard == null) {
            secondCard = cardsInGame.get(cardPosition);
            rotate(cardPosition, cardsInGame.get(cardPosition).getImage(), 0);
        } else {
            thirdCard = cardsInGame.get(cardPosition);
            rotate(cardPosition, cardsInGame.get(cardPosition).getImage(), 0);
            CardsAreFlipped = true;
            checkForMatch();
        }
    }
    @Override
    public void checkForMatch(){

        if (firstCard.sameCardAs(secondCard) && firstCard.sameCardAs(thirdCard)){
            System.out.println("same");
            CardsAreFlipped = false;

            //hier noch Player update einfügen
            firstCard.setCorrectPair(true);
            secondCard.setCorrectPair(true);
            thirdCard.setCorrectPair(true);
        } else {
            rotateBack();
        }

        firstCard = null;
        secondCard = null;
        thirdCard = null;
        PauseTransition delay = new PauseTransition(Duration.millis(1500));
        delay.play();
        delay.setOnFinished(delayEvent ->{
            CardsAreFlipped = false;});
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
}


