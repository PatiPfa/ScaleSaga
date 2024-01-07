package com.example.memoryprototyp1.GameModi;

import com.example.memoryprototyp1.Card;
import com.example.memoryprototyp1.CardDeck;
import com.example.memoryprototyp1.MemoryCard;
import com.example.memoryprototyp1.Player;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;

public class MultiplayerForTwo_2Cards extends BaseGame {

    int gameCounter;

    public MultiplayerForTwo_2Cards(int flowPaneSize, FlowPane imagesFlowPane) {
        super(flowPaneSize, imagesFlowPane);
    }

    @Override
    public void play(){
        firstCard = null;
        secondCard = null;
        CardDeck deck = new CardDeck();
        deck.shuffle();
        cardsInGame = new ArrayList<>();
        cardsAreFlipped = false;


        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        player1.setColor(Color.RED);
        player2.setColor(Color.BLUE);

        for (int i = 0; i < flowPaneSize / 2; i++) {
            Card topCardFromDeck = deck.giveTopCard();

            cardsInGame.add(new MemoryCard(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));
            cardsInGame.add(new MemoryCard(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));

        }
        Collections.shuffle(cardsInGame);
        System.out.println(cardsInGame);
        rotateAllCardsToBackSide();
    }

}
