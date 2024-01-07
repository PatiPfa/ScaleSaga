package com.example.memoryprototyp1;

import java.util.ArrayList;

public class MemoryCard extends Card{

    private boolean correctPair;

    public boolean isCorrectPair() {
        return correctPair;
    }

    public void setCorrectPair(boolean correctPair) {
        this.correctPair = correctPair;
    }

    public MemoryCard(String name) {
        super(name);
        this.correctPair = false;
    }
    public boolean sameCardAs(MemoryCard secondCard){
        return (this.getName().equals(secondCard.getName()));
    }

    }
