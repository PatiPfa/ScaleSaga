package com.example.memoryprototyp1;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class MemoryCard extends Card{

    private boolean correctPair;

    public boolean isCorrectPair() {
        return correctPair;
    }

    public void setCorrectPair(boolean correctPair) {
        this.correctPair = correctPair;
    }

    public MemoryCard(String name, Image frontOfCards) {
        super(name, frontOfCards);
        this.correctPair = false;
    }
    public boolean sameCardAs(MemoryCard secondCard){
        return (this.getName().equals(secondCard.getName()));
    }

    }
