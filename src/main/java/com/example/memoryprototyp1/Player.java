package com.example.memoryprototyp1;

import javafx.scene.paint.Color;

public class Player {
    private final String name;
    private int points;
    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

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

    public void setPoints(int points) {
        this.points = points;
    }

    public void addOnePoint(){
        points ++;
    }
}



