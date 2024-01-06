package com.example.memoryprototyp1;

import java.util.ArrayList;
import java.util.Collections;

public class CardDeck {
    private ArrayList<Card> deck;

    /**
     * number of usable card-images in src/main/resources/com/example/memoryprototyp1/imagese
     */

    private final int NUMBER_OF_IMAGES = 12;

    public CardDeck(){
        this.deck = new ArrayList<>();

        for (int i = 1; i <= NUMBER_OF_IMAGES ; i++) {
            deck.add(new Card("card" + i + ".png" ));
        }
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }


    /**
     * This method shuffles the cards
     */
    public void shuffle(){
        Collections.shuffle(deck);
    }

    /**
     * This method returns the top card from the deck.
     */
    public  Card giveTopCard(){
        if (!deck.isEmpty()){
            return deck.remove(0);
        } else {
            return null;
        }


    }

}
