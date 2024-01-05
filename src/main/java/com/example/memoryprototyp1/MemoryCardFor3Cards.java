package com.example.memoryprototyp1;

public class MemoryCardFor3Cards extends MemoryCard {

    private boolean correctThreePairs;
    public MemoryCardFor3Cards(String name) {
        super(name);
    }
    @Override
    public void setCorrectPair(boolean correctThreePairs) {
        this.correctThreePairs = correctThreePairs;
    }


}
