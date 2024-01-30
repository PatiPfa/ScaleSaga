package com.example.memoryprototyp1;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class CardDeck {
    private ArrayList<Card> deck;

    private static final int NUMBER_OF_IMAGES = 18; //<-- Anzahl der benutzbaren Karten in
                                                    // src/main/resources/com/example/memoryprototyp1/images

    public static int getNUMBER_OF_IMAGES() {
        return NUMBER_OF_IMAGES;
    }

    public CardDeck(){
        this.deck = new ArrayList<>();

        for (int i = 1; i <= NUMBER_OF_IMAGES ; i++) {
            deck.add(new Card("card" + i + ".png" ,
                    new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/card"+ i +".png")))));
        }
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }


    /**
     * Diese Methode mischt die Karten
     */
    public void shuffle(){
        Collections.shuffle(deck);
    }

    /**
     * Diese Methode gibt die oberste Karte zurück
     */
    public  Card giveTopCard(){
        if (!deck.isEmpty()){
            return deck.remove(0);
        } else {
            return null;
        }


    }


}
