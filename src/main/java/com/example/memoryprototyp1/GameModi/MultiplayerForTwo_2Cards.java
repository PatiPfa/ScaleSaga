package com.example.memoryprototyp1.GameModi;

import com.example.memoryprototyp1.Card;
import com.example.memoryprototyp1.CardDeck;
import com.example.memoryprototyp1.Player;
import com.example.memoryprototyp1.*;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import static com.example.memoryprototyp1.Card.getBackOfCardsImage;
import static com.example.memoryprototyp1.Music.MusicPlayer.playButtonSound;

public class MultiplayerForTwo_2Cards extends BaseGame {

    private Stage stage;
    private Scene scene;
    private Parent root;
    Player player1;
    Player player2;
    Player playerOnTurn;
    private Label player1PointsLabel;
    private Label player2PointsLabel;
    private Label playerOnTurnLabel;
    private Label player1name;
    private Label player2name;
    private ImageView iv_lastcardp1;
    private ImageView iv_lastcardp2;
    private int lastClickedCard;
    private AnchorPane popUp;
    private String currentCursor = "sword";
    private final Image CURSOR_SWORD = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/sword.png")));
    private final Image CURSOR_AXE = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/axe.png")));
    private Text name;
    private String CursorPlayer1;
    private String CursorPlayer2;
    private boolean firstRound = true;
    private ImageView iv_player1symbol;
    private ImageView iv_player2symbol;
    private static boolean delayStart = false;

    public MultiplayerForTwo_2Cards(int size, FlowPane imagesFlowPane, Label player1PointsLabel, Label player2PointsLabel, Label playerOnTurnLabel, Label player1name, Label player2name, ImageView iv_lastcardp1, ImageView iv_lastcardp2, AnchorPane popUp, Text name, ImageView iv_player1symbol, ImageView iv_player2symbol) {
        super(size, imagesFlowPane);
        this.player1PointsLabel = player1PointsLabel;
        this.player2PointsLabel = player2PointsLabel;
        this.playerOnTurnLabel = playerOnTurnLabel;
        this.player1name = player1name;
        this.player2name = player2name;
        this.iv_lastcardp1 = iv_lastcardp1;
        this.iv_lastcardp2 = iv_lastcardp2;
        this.popUp = popUp;
        this.name = name;
        this.iv_player1symbol = iv_player1symbol;
        this.iv_player2symbol = iv_player2symbol;
    }


    /**
     *  Methode initialisiert ImageView-Objekte, die in einem FlowPane angeordnet sind.
     */
    @Override
    public void initializeImageView() {


        for (int i = 0; i < imagesFlowPane.getChildren().size(); i++) {                                                 //startet for schleife welche alle ImageView Objekte des FlowPanes durchläuft
            ImageView imageView = (ImageView) imagesFlowPane.getChildren().get(i);                                      //holt i-tes ImageView Objekt aus FlowPane und wandelt es in ImageView um
            imageView.setImage(getBackOfCardsImage());                                                                  //Setzt Bild des ImageViews auf Rückseite der Karte
            imageView.setUserData(i);

            imageView.setOnMouseEntered(mouseEnteredEvent ->{                                                           //Karte wird leicht vergroßert wenn Maus drüberfahrt und sie noch nicht aufgedeckt ist
                if (!this.getCardsInGame().get((int) imageView.getUserData()).getRevealed()){
                    this.setImageScale((int) imageView.getUserData(), 1.05);
                }
            });

            imageView.setOnMouseExited(mouseEnteredEvent ->{                                                            //macht Karten kleiner wieder wenn Maus weg ist
                this.setImageScale((int) imageView.getUserData(), 1);
            });

            imageView.setOnMouseClicked(mouseEvent -> {                                                                 //bei Mausklick wird geprüft ob KArte noch nicht aufgedeckt ist, ob keine andere Karten gerade umgedreht werden
                if ((delayStart && !this.getCardsInGame().get((int) imageView.getUserData()).getRevealed()) && !this.getCardsAreFlipped()){                             //Bedingungen erfüllt = Karte umgedreht
                    this.flipCard((int) imageView.getUserData());
                    lastClickedCard = (int) imageView.getUserData();
                }
            });
        }
    }

    /**
     * Gamelogic von Multiplayer
     */
    @Override
    public void play() {
        delayStart = false;
        PauseTransition initialDelay = new PauseTransition(Duration.seconds(3));                                     //delay von 3 Sek am Anfang des Spiels
        initialDelay.setOnFinished(event -> {
            delayStart = true;
        });
        initialDelay.play();

        player1 = new Player(MainMenuController.getPlayer1name());                                                      //Spieler Objekte erstellt; Namen aus MainMenuController geholt
        player2 = new Player(MainMenuController.getPlayer2name());

        Random random = new Random();                                                                                   //Zufällig entschieden welcher Spieler drankommt
        int randomStart = random.nextInt(2) + 1;
        playerOnTurn = (randomStart == 1) ? player1 : player2;

        if(firstRound && randomStart == 1){                                                                             //welcher Spieler welcher Cursor
            CursorPlayer1 = "sword";
            CursorPlayer2 = "axe";
            firstRound = false;
        }else if (firstRound){
            CursorPlayer1 = "axe";
            CursorPlayer2 = "sword";
            iv_player1symbol.setImage(CURSOR_AXE);
            iv_player2symbol.setImage(CURSOR_SWORD);
            firstRound = false;
        }else if((randomStart == 1 && !currentCursor.equals(CursorPlayer1)) || (randomStart == 1 && !currentCursor.equals(CursorPlayer2))){
            switchCursor();
        }

        player1name.setText(player1.getName());                                                                         //Text als Spielername und Farbe des aktiven Spieler
        player2name.setText(player2.getName());

        if(playerOnTurn.equals(player1)){
            player1name.setTextFill(Color.DARKGREEN);
        }else{
            player2name.setTextFill(Color.DARKGREEN);
        }

        updatePointsLabels();                                                                                           //aktualisiert Punltezahl der Spieler
        updatePlayerOnTurnLabel();

        firstCard = null;
        secondCard = null;
        CardDeck deck = new CardDeck();
        deck.shuffle();
        cardsInGame = new ArrayList<>();
        cardsAreFlipped = false;

        for (int i = 0; i < flowPaneSize / 2; i++) {                                                                    //fügt Paare von Karten dem Spiel zu
            Card topCardFromDeck = deck.giveTopCard();

            cardsInGame.add(new Card(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));
            cardsInGame.add(new Card(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));

        }
        Collections.shuffle(cardsInGame);                                                                               //dreht Karten wieder um
        System.out.println(cardsInGame);
        rotateAllCardsToBackSide();
        rotateDisplayImageView(iv_lastcardp1, getBackOfCardsImage());
        rotateDisplayImageView(iv_lastcardp2, getBackOfCardsImage());
    }

    /**
     * checkt ob Karten gleich sind
     */
    @Override
    public void checkForMatch(){                                                                                        //überprüft ob aufgedeckte Karten gleich sind
        if (firstCard.sameCardAs(secondCard)){
            System.out.println("same");
            playButtonSound();
            cardsAreFlipped = false;                                                                                    //setzt das alle Karten umgedreht sind

            if(playerOnTurn.equals(player1)){
                player1.addOnePoint();
                rotateDisplayImageView(iv_lastcardp1, cardsInGame.get(lastClickedCard).getFrontOfCards());
            }else{
                player2.addOnePoint();
                rotateDisplayImageView(iv_lastcardp2, cardsInGame.get(lastClickedCard).getFrontOfCards());
            }
            updatePlayerOnTurn();
        } else {                                                                                                        //wenn Karten nicht matchen dann wieder umdrehen
            rotateBack();
        }

        firstCard = null;
        secondCard = null;

        updatePointsLabels();

        if(allCardsFlipped()){                                                                                          //checkt ob alle Karten umgedreht sind; Methode Winner wird aufgerufen
            winner();
        }

        updatePlayerOnTurn();                                                                                           //aktualisiert wer dran is
        System.out.println(playerOnTurn.getName());
        System.out.println("Player 1: " + player1.getPoints());
        System.out.println("Player 2: " + player2.getPoints());

        PauseTransition delay = new PauseTransition(Duration.millis(1500));                                          //pause bis Karten umgedreht werden
        delay.play();
        delay.setOnFinished(delayEvent ->{
            cardsAreFlipped = false;
        });

        updatePlayerOnTurnLabel();
    }


    /**
     * Updated Punkte der Player
     */
    private void updatePointsLabels(){
        player1PointsLabel.setText(Integer.toString(player1.getPoints()));
        player2PointsLabel.setText(Integer.toString(player2.getPoints()));
    }


    /**
     * Updated Player Namen
     */
    private void updatePlayerOnTurnLabel(){
        playerOnTurnLabel.setText(playerOnTurn.getName());
    }

    /**
     * Erkennt welcher Spieler gewonnen hat und lässt PopUp mit Gewinnbenachrichtigung erscheinen
     */
    private void winner(){
        String winner;

        if(player1.getPoints() > player2.getPoints()){
            winner = player1.getName();
            name.setText(winner);
        }else if(player1.getPoints() < player2.getPoints()){
            winner = player2.getName();
            name.setText(winner);
        }else{
            winner = "DRAW";
            name.setText(winner);
        }
        System.out.println(winner);


        popUp.setVisible(!popUp.isVisible());
    }

    /**
     * ändert Cursor
     */
    public void switchCursor(){
        try{
            Scene scene =  playerOnTurnLabel.getScene();                                                                //wenn currentCursor sword dann auf axe geswitched und wenn axe dann auf sword
            if(currentCursor.equals("sword")){
                scene.setCursor(new ImageCursor(CURSOR_AXE));
                currentCursor = "axe";
            } else {
                scene.setCursor(new ImageCursor(CURSOR_SWORD));
                currentCursor = "sword";
            }
        }catch (Exception e){
            MainMenuController.writeInLog(e, "switchCursor");
        }

    }

    /**
     * Spieler, der an der Reihe ist, zu wechseln
     */
    public void updatePlayerOnTurn(){
        if(playerOnTurn.equals(player1)){
            playerOnTurn = player2;
            player1name.setTextFill(Color.WHITE);
            player2name.setTextFill(Color.DARKGREEN);
        }else {
            playerOnTurn = player1;
            player1name.setTextFill(Color.DARKGREEN);
            player2name.setTextFill(Color.WHITE);
        }
    }

    /**
     * animierte Rotation der Karten
     */
    public void rotateDisplayImageView(ImageView imageView, Image imageToBeShown) {


        TranslateTransition translate = new TranslateTransition();                                                      //bewegt die Karte leicht anch oben und dann nach unten
        RotateTransition rotateFirstHalf = new RotateTransition();


        translate.setNode(imageView);
        translate.setByY(-10);
        translate.setDuration(Duration.millis(200));

        translate.play();

        translate.setOnFinished(eventTranslateBack -> {
            TranslateTransition translateBack = new TranslateTransition();
            translateBack.setNode(imageView);
            translateBack.setDuration(Duration.millis(100));
            translateBack.setByY(10);
            translateBack.play();
        });

        rotateFirstHalf.setNode(imageView);                                                                             //erste Hälfte der Drehung wird ausgeführt
        rotateFirstHalf.setDuration(Duration.millis(200));
        rotateFirstHalf.setByAngle(90);
        rotateFirstHalf.setAxis(Rotate.Y_AXIS);
        rotateFirstHalf.play();
        rotateFirstHalf.setOnFinished(eventRotateSecondHalf -> {
            RotateTransition rotateSecondHalf = new RotateTransition();                                                 //Drehung wird vervollständigt
            rotateSecondHalf.setNode(imageView);
            imageView.setImage(imageToBeShown);
            rotateSecondHalf.setDelay(Duration.seconds(0));
            rotateSecondHalf.setDuration(Duration.millis(200));
            rotateSecondHalf.setByAngle(-90);
            rotateSecondHalf.play();
        });
    }


}
