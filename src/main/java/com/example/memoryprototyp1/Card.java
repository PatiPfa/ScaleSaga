package com.example.memoryprototyp1;

import javafx.scene.image.Image;

import java.util.Objects;

public class Card {

    private String name;
    private boolean isRevealed;

    private static final Image backOfCards = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/back.png")));
    private Image frontOfCards;

    public Card(String name, Image frontOfCards) {
        this.name = name;
        this.frontOfCards = frontOfCards;
        this.isRevealed = false;
    }


    public String getName() {
        return name;
    }

    public boolean getRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public static Image getBackOfCards(){
        return backOfCards;
    }

    public Image getFrontOfCards(){
        return frontOfCards;
    }
}
