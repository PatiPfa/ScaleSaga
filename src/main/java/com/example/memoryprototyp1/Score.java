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


    public Score(int scoreMin, int scoreSec, String player) {
        this.scoreMin = scoreMin;
        this.scoreSec = scoreSec;
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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
            System.out.println("Irdendwos geht ni ");
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
            System.out.println("Something is wrong");
        }

        return out;
    }
}