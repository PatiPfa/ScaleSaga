package com.example.memoryprototyp1;

import javafx.scene.paint.Color;

public class Player {
    private final String name;
    private int points;

    public Player(String name) {
        this.name = name;
        this.points = 0;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void addOnePoint(){
        this.points ++;
    }
}



