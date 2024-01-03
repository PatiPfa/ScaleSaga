package com.example.memoryprototyp1;

import javafx.scene.image.Image;

import static com.example.memoryprototyp1.Card.getBackOfCards;

public class test {
    public static void main(String[] args) {

        CardDeck test1 = new CardDeck();
        test1.shuffle();

        for (Card card : test1.getDeck()) {
            System.out.println(card.getName());
        }
        System.out.println("********************");


        for (int i = 0; i <4 ; i++) {
            System.out.println(test1.giveTopCard());
        }

        System.out.println("********************");


        for (int i = 0; i <4 ; i++) {
            System.out.println(test1.giveTopCard());
        }
    }
}
