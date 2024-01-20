package com.example.memoryprototyp1;

public class ScoreBoard {

    private int score;
    private Player player;

    public ScoreBoard(int score, Player player) {
        this.score = score;
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
