package com.example.memoryprototyp1;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class CardDeck {
    private ArrayList<Card> deck;

    /**
     * number of usable card-images in src/main/resources/com/example/memoryprototyp1/images
     */

    private static final int NUMBER_OF_IMAGES = 15;

    public static int getNUMBER_OF_IMAGES() {
        return NUMBER_OF_IMAGES;
    }

    public CardDeck(){
        this.deck = new ArrayList<>();

        for (int i = 1; i <= NUMBER_OF_IMAGES ; i++) {
            deck.add(new Card("card" + i + ".png" , new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/card"+ i +".png")))));
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
