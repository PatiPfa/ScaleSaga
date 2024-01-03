package com.example.memoryprototyp1;

import javafx.scene.image.Image;

import java.util.Objects;

public class Card {

    private String name;
    private boolean isRevealed;

    private static final Image backOfCards = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/back.png")));

    public Card(String name) {
        this.name = name;
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

    public Image getImage(){
        String pathName = "images/" + this.getName();
        return new Image(Objects.requireNonNull(Card.class.getResourceAsStream(pathName)));
    }
}
