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

    /**
     * constructor
     **/
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

        //startet for schleife welche alle ImageView Objekte des FlowPanes durchläuft
        for (int i = 0; i < imagesFlowPane.getChildren().size(); i++) {
            //holt i-tes ImageView Objekt aus FlowPane und wandelt es in ImageView um
            ImageView imageView = (ImageView) imagesFlowPane.getChildren().get(i);
            //Setzt Bild des ImageViews auf Rückseite der Karte
            imageView.setImage(getBackOfCardsImage());
            //setzt i als user Data
            imageView.setUserData(i);

            //Karte wird leicht vergrößert wenn Maus drüberfahrt und sie noch nicht aufgedeckt ist
            imageView.setOnMouseEntered(mouseEnteredEvent ->{
                if (!this.getCardsInGame().get((int) imageView.getUserData()).getRevealed()){
                    this.setImageScale((int) imageView.getUserData(), 1.05);
                }
            });

            //macht Karten wieder kleiner, wenn Maus weg ist
            imageView.setOnMouseExited(mouseEnteredEvent ->{
                this.setImageScale((int) imageView.getUserData(), 1);
            });

            //bei Mausklick wird geprüft ob Karte noch nicht aufgedeckt ist, ob keine andere Karten gerade umgedreht werden
            imageView.setOnMouseClicked(mouseEvent -> {
                if ((delayStart && !this.getCardsInGame().get((int) imageView.getUserData()).getRevealed()) && !this.getCardsAreFlipped()){
                    this.flipCard((int) imageView.getUserData());
                    lastClickedCard = (int) imageView.getUserData();
                }
            });
        }
    }

    /**
     * Startmethode von Multiplayer
     */
    @Override
    public void play() {
        //delay von 3 Sek am Anfang des Spiels
        delayStart = false;
        PauseTransition initialDelay = new PauseTransition(Duration.seconds(1));
        initialDelay.setOnFinished(event -> {
            delayStart = true;
        });
        initialDelay.play();

        //Spieler Objekte erstellt; Namen aus MainMenuController geholt
        player1 = new Player(MainMenuController.getPlayer1name());
        player2 = new Player(MainMenuController.getPlayer2name());

        //Zufällig entschieden welcher Spieler drankommt
        Random random = new Random();
        int randomStart = random.nextInt(2) + 1;
        playerOnTurn = (randomStart == 1) ? player1 : player2;

        //in der ersten runde bekommt der spieler welcher beginnt den Schwert Cursor
        if(firstRound && randomStart == 1){
            CursorPlayer1 = "sword";
            CursorPlayer2 = "axe";
            firstRound = false;
        }else if (firstRound){
            CursorPlayer1 = "axe";
            CursorPlayer2 = "sword";
            //Standartmäßig hat player eins das Schwert Icon, hier wird es umgedreht
            iv_player1symbol.setImage(CURSOR_AXE);
            iv_player2symbol.setImage(CURSOR_SWORD);
            firstRound = false;
            //falls es nicht die erste Runde ist (sondern play again gedrückt wurde), wird überprüft ob der Spieler der beginnt, den selben Cursor hat und wenn nötig der Cursor getauscht
        }else if((randomStart == 1 && !currentCursor.equals(CursorPlayer1)) || (randomStart == 1 && !currentCursor.equals(CursorPlayer2))){
            switchCursor();
        }

        //Spielernamen schreiben
        player1name.setText(player1.getName());
        player2name.setText(player2.getName());

        //setzt den beginnenten Spielernamen auf grün
        if(playerOnTurn.equals(player1)){
            player1name.setTextFill(Color.DARKGREEN);
        }else{
            player2name.setTextFill(Color.DARKGREEN);
        }

        //aktualisiert Punktezahl der Spieler sowie den Spieler, welcher am Zug ist
        updatePointsLabels();
        updatePlayerOnTurnLabel();

        //Karten null setzen, deck erstellen und mischen
        firstCard = null;
        secondCard = null;
        CardDeck deck = new CardDeck();
        deck.shuffle();
        cardsInGame = new ArrayList<>();
        cardsAreFlipped = false;

        //fügt Paare von Karten dem Spiel zu
        for (int i = 0; i < flowPaneSize / 2; i++) {
            Card topCardFromDeck = deck.giveTopCard();
            cardsInGame.add(new Card(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));
            cardsInGame.add(new Card(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));
        }
        //mischt die Karten, welche im Spiel sind
        Collections.shuffle(cardsInGame);
        //dreht Karten um
        rotateAllCardsToBackSide();
        rotateDisplayImageView(iv_lastcardp1, getBackOfCardsImage());
        rotateDisplayImageView(iv_lastcardp2, getBackOfCardsImage());
    }

    /**
     * checkt ob Karten gleich sind
     */
    @Override
    public void checkForMatch(){
        //überprüft, ob aufgedeckte Karten gleich sind
        if (firstCard.sameCardAs(secondCard)){
            playButtonSound();
            //setzt das alle Karten umgedreht sind
            cardsAreFlipped = false;

            //überprüft welcher Spieler aktuell dran ist, fügt diesem 1 Punkt hinzu und die gefundene Karte wird groß angezeigt
            if(playerOnTurn.equals(player1)){
                player1.addOnePoint();
                rotateDisplayImageView(iv_lastcardp1, cardsInGame.get(lastClickedCard).getFrontOfCards());
            }else{
                player2.addOnePoint();
                rotateDisplayImageView(iv_lastcardp2, cardsInGame.get(lastClickedCard).getFrontOfCards());
            }

            switchCursor();
            updatePlayerOnTurn();

        } else {
            //wenn Karten nicht matchen dann wieder umdrehen
            rotateBack();
        }

        //beide karten wieder "null" setzen
        firstCard = null;
        secondCard = null;

        //Punkte Labels aktualisieren
        updatePointsLabels();

        //checkt ob alle Karten umgedreht sind; Methode Winner wird aufgerufen
        if(allCardsFlipped()){
            winner();
        }

        //aktualisiert wer dran is
        updatePlayerOnTurn();

        //pause bis Karten umgedreht werden
        PauseTransition delay = new PauseTransition(Duration.millis(1500));
        if (cardsAreFlipped){
            delay.play();
            delay.setOnFinished(delayEvent ->{
                cardsAreFlipped = false;
            });
        }

        updatePlayerOnTurnLabel();
        switchCursor();
    }


    /**
     * Updated Punkte der Player Labels
     */
    private void updatePointsLabels(){
        player1PointsLabel.setText(Integer.toString(player1.getPoints()));
        player2PointsLabel.setText(Integer.toString(player2.getPoints()));
    }


    /**
     * Updated Player Namen vom Spieler am Zug
     */
    private void updatePlayerOnTurnLabel(){
        playerOnTurnLabel.setText(playerOnTurn.getName());
    }

    /**
     * Erkennt welcher Spieler gewonnen hat und lässt PopUp mit Gewinnbenachrichtigung erscheinen
     */
    private void winner(){
        String winner;

        //Überprüft welcher Spieler gewonnen hat und setzt das Textlabel
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

        popUp.setVisible(!popUp.isVisible());
    }

    /**
     * ändert Cursor
     */
    public void switchCursor(){
        try{
            Scene scene =  playerOnTurnLabel.getScene();
            //wenn currentCursor sword dann auf axe geswitched und wenn axe dann auf sword
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
        //wechselt den Spieler on Turn auf den jeweils anderen und ändert die Farbe
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

        TranslateTransition translate = new TranslateTransition();
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

        //erste Hälfte der Drehung wird ausgeführt
        rotateFirstHalf.setNode(imageView);
        rotateFirstHalf.setDuration(Duration.millis(200));
        rotateFirstHalf.setByAngle(90);
        rotateFirstHalf.setAxis(Rotate.Y_AXIS);
        rotateFirstHalf.play();
        rotateFirstHalf.setOnFinished(eventRotateSecondHalf -> {
            //Drehung wird vervollständigt
            RotateTransition rotateSecondHalf = new RotateTransition();
            rotateSecondHalf.setNode(imageView);
            imageView.setImage(imageToBeShown);
            rotateSecondHalf.setDelay(Duration.seconds(0));
            rotateSecondHalf.setDuration(Duration.millis(200));
            rotateSecondHalf.setByAngle(-90);
            rotateSecondHalf.play();
        });
    }

}
