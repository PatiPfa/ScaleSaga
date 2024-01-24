package com.example.memoryprototyp1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static com.example.memoryprototyp1.MainMenuController.getGamemode;

public class Score implements Serializable{

    private int scoreSec;
    private int scoreMin;
    private String player;
    private static String txtFile;



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
/**
  *Gibt den Pfad zur Textdatei des aktuellen Spielmodus zurück für den Richtigen Modus.
 **/
    public static String getCurrentGameMode() {
        String currentGameMode = getGamemode();
        switch (currentGameMode) {
            case "Singleplayer2Cards":
                return txtFile =  "src/main/resources/com/example/memoryprototyp1/score/scoreTwoCards.txt";
            case "Singleplayer3Cards":
                return txtFile = "src/main/resources/com/example/memoryprototyp1/score/scoreThreeCards.txt";
            default:
                return null;
        }
    }
    /**
    *Serialisiert den Score-Array und speichert die Textdatei des aktuellen Spielmodus.
    **/
    public static void serializeScore(Score[] s){
     txtFile = getCurrentGameMode();

     try {
         FileOutputStream fileOut = new FileOutputStream(txtFile);  //von byte in datein
         ObjectOutputStream out = new ObjectOutputStream(fileOut);  //objekten in datein
         out.writeObject(s);                                            // Schreibt das Score-Array in die Textdatei
         out.close();
         fileOut.close();
     } catch (Exception e) {
         System.out.println("Irdendwos geht nit ");
         throw new RuntimeException(e);

     }
 }
/**
*Deserialisiert das Score-Array aus der entsprechenden Textdatei des aktuellen Spielmodus.
**/
 public static Score[] deserializeScore(){
     Score[] out = new Score[5];
     txtFile = getCurrentGameMode();

     try {
         assert txtFile != null;
         FileInputStream fileIn = new FileInputStream(txtFile);     //auslesen von byte
         ObjectInputStream in = new ObjectInputStream(fileIn);      //lesen von objekten
         out = (Score[]) in.readObject();                           // wird hier ausgelesen
     } catch (Exception e){
         System.out.println("Array is empty");
     }

     return out;
 }
/**
* Fügt eine Null bei den Sekunden hinzu, dass es zweistellig bleibt
**/
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

