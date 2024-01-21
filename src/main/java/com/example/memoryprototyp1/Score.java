package com.example.memoryprototyp1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Score implements Serializable{

    private int scoreSec;
    private int scoreMin;
    private String player;



    private static Score[] scoreBoard = new Score[5];

    public static void setScoreBoard(Score[] scoreBoard) {
        Score.scoreBoard = scoreBoard;
    }

    public Score(int scoreMin, int scoreSec, String player) {
        this.scoreMin = scoreMin;
        this.scoreSec = scoreSec;
        this.player = player;
    }

    public int getScoreMin() {
        return scoreMin;
    }

    public int getScoreSec() {
        return scoreSec;
    }

    public String getPlayerName(){
        return this.player;
    }
    public static Score[] getScoreBoard() {
        return scoreBoard;
    }

    public static void serializeScore(Score[] s){
        try {
            FileOutputStream fileOut = new FileOutputStream("src/main/resources/com/example/memoryprototyp1/score/score.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(s);
            out.close();
            fileOut.close();
        } catch (Exception e) {
            System.out.println("Irdendwos geht nit ");
            throw new RuntimeException(e);
        }
    }

    public static Score[] deserializeScore(){
        Score[] out = new Score[5];
        try {
            FileInputStream fileIn = new FileInputStream("src/main/resources/com/example/memoryprototyp1/score/score.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            out = (Score[]) in.readObject();
        } catch (Exception e){
            System.out.println("Array is empty");
        }

        return out;
    }

    public int scoreToNumber(){
        String temp;
        if (this.scoreSec < 10){
           temp = Integer.toString(this.scoreMin) + "0" + Integer.toString(this.scoreSec);
        } else {
           temp = Integer.toString(this.scoreMin) + Integer.toString(this.scoreSec);
        }

        return Integer.parseInt(temp);
    }
}