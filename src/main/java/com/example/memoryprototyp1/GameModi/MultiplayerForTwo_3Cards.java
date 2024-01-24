package com.example.memoryprototyp1.GameModi;

import com.example.memoryprototyp1.*;
import javafx.animation.PauseTransition;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import static com.example.memoryprototyp1.Card.getBackOfCardsImage;
import static com.example.memoryprototyp1.Music.MusicPlayer.playButtonSound;

public class MultiplayerForTwo_3Cards extends BaseGame {
    private AnchorPane popUp2;
    private ImageView iv_player1symbol;
    private ImageView iv_player2symbol;
    private Label player1PointsLabel;
    private Label player2PointsLabel;
    private Label playerOnTurnLabel;
    private Label player1name;
    private Label player2name;
    private Text name2;

    private Card thirdCard;
    Player player1;
    Player player2;
    Player playerOnTurn;

    private boolean firstRound = true;
    private boolean delayStart = false;
    private String currentCursor = "sword";
    private String CursorPlayer1;
    private String CursorPlayer2;

    private final Image CURSOR_SWORD = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/sword.png")));
    private final Image CURSOR_AXE = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/axe.png")));


    /**
     * constructor
     **/
    public MultiplayerForTwo_3Cards(int flowPaneSize, FlowPane imagesFlowPane, Label player1PointsLabel, Label player2PointsLabel, Label playerOnTurnLabel, Label player1name, Label player2name, AnchorPane popUp2, Text name2, ImageView iv_player1symbol, ImageView iv_player2symbol) {
        super(flowPaneSize, imagesFlowPane);
        this.player1PointsLabel = player1PointsLabel;
        this.player2PointsLabel = player2PointsLabel;
        this.playerOnTurnLabel = playerOnTurnLabel;
        this.player1name = player1name;
        this.player2name = player2name;
        this.popUp2 = popUp2;
        this.name2 = name2;
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
                if ((delayStart && !this.getCardsInGame().get((int) imageView.getUserData()).getRevealed()) && !cardsAreFlipped){
                    this.flipCard((int) imageView.getUserData());
                }
            });
        }
    }

    /**
     * Startmethode von Multiplayer 3 Cards
     */
    @Override
    public void play(){
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

        //Zufällig entschieden welcher Spieler beginnt
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
            //falls es nicht die erste Runde ist (sondern play again gedrückt wurde),
            // wird überprüft ob der Spieler der beginnt, denselben Cursor hat und wenn nötig der Cursor getauscht
        }else if((randomStart == 1 && !currentCursor.equals(CursorPlayer1)) || (randomStart == 2 && !currentCursor.equals(CursorPlayer2))){
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
        thirdCard = null;
        CardDeck deck = new CardDeck();
        deck.shuffle();
        cardsInGame = new ArrayList<>();
        cardsAreFlipped = false;

        //fügt Paare von Karten dem Spiel zu
        for (int i = 0; i < flowPaneSize / 3; i++) {
            Card topCardFromDeck = deck.giveTopCard();

            cardsInGame.add(new Card(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));
            cardsInGame.add(new Card(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));
            cardsInGame.add(new Card(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));

        }
        //mischt die Karten, welche im Spiel sind
        Collections.shuffle(cardsInGame);
        //dreht Karten um
        rotateAllCardsToBackSide();
    }

    /**
     *
     * flipt die Karte
     **/
    @Override
    public void flipCard(int cardPosition) {

        cardsInGame.get(cardPosition).setRevealed(true);

        //nimmt die Karte, welche derzeit dran ist (first-third card) und rotiert diese
        if (firstCard == null) {
            firstCard = cardsInGame.get(cardPosition);
            rotate(cardPosition, cardsInGame.get(cardPosition).getFrontOfCards(), 0);
        } else if (secondCard == null) {
            secondCard = cardsInGame.get(cardPosition);
            rotate(cardPosition, cardsInGame.get(cardPosition).getFrontOfCards(), 0);
        } else {
            thirdCard = cardsInGame.get(cardPosition);
            rotate(cardPosition, cardsInGame.get(cardPosition).getFrontOfCards(), 0);
            cardsAreFlipped = true;
            //wenn es die dritte Karte ist, wird überprüft, ob die 3 Karten gleich sind
            checkForMatch();
        }
    }

    /**
     * checkt ob Karten gleich sind
     */
    @Override
    public void checkForMatch(){
        //überprüft, ob aufgedeckte Karten gleich sind
        if (firstCard.sameCardAs(secondCard) && firstCard.sameCardAs(thirdCard)){
            playButtonSound();
            //setzt das alle Karten umgedreht sind
            cardsAreFlipped = false;

            //überprüft welcher Spieler aktuell dran ist, fügt diesem 1 Punkt hinzu
            if(playerOnTurn.equals(player1)){
                player1.addOnePoint();
            }else{
                player2.addOnePoint();
            }

            updatePlayerOnTurn();
            switchCursor();

        } else {
            //wenn Karten nicht matchen dann wieder umdrehen
            rotateBack();
        }

        //Alle Karten wieder "null" setzen
        firstCard = null;
        secondCard = null;
        thirdCard = null;

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
        if (cardsAreFlipped) {
            delay.play();
            delay.setOnFinished(delayEvent ->{
                cardsAreFlipped = false;});
        }

        updatePlayerOnTurnLabel();
        switchCursor();

    }

    /**
     * dreht die Karten wieder um
     **/
    public void rotateBack(){
        //index holen
        int indexFirstCard = cardsInGame.indexOf(firstCard);
        int indexSecondCard = cardsInGame.indexOf(secondCard);
        int indexThirdCard = cardsInGame.indexOf(thirdCard);
        PauseTransition delay = new PauseTransition(Duration.millis(1500)); //<- time how long the cards are revealed
        delay.play();
        delay.setOnFinished(delayEvent ->{
            //Karten rotieren und Bild auf Rückseite setzen
            rotate(indexFirstCard, getBackOfCardsImage(), 0);
            rotate(indexSecondCard, getBackOfCardsImage(), 0);
            rotate(indexThirdCard, getBackOfCardsImage(), 0);
            PauseTransition delay2 = new PauseTransition(Duration.millis(485));//<- after delay setRevealed is set false, this prevents card flip bugs
            delay2.play();
            delay2.setOnFinished(cardsAreFlippedBack ->{
                //setzt alle Karten auf nicht revealed
                cardsInGame.get(indexFirstCard).setRevealed(false);
                cardsInGame.get(indexSecondCard).setRevealed(false);
                cardsInGame.get(indexThirdCard).setRevealed(false);
            });
        });
    }

    /**
     * Updated Punkte der Player
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
    private void winner() {
        String winner;

        //Überprüft welcher Spieler gewonnen hat und setzt das Textlabel
        if (player1.getPoints() > player2.getPoints()) {
            winner = player1.getName();
            name2.setText(winner);
        } else if (player1.getPoints() < player2.getPoints()) {
            winner = player2.getName();
            name2.setText(winner);
        } else {
            winner = "DRAW";
            name2.setText(winner);
        }

        //zeigt das gewinner Popup an

        popUp2.setVisible(!popUp2.isVisible());

    }

    /**
     * ändert Cursor
     */
    public void switchCursor() {
        try {
            Scene scene = playerOnTurnLabel.getScene();

            //wenn currentCursor sword dann auf axe geswitched und wenn axe dann auf sword
            if (currentCursor.equals("sword")) {
                scene.setCursor(new ImageCursor(CURSOR_AXE));
                currentCursor = "axe";
            } else {
                scene.setCursor(new ImageCursor(CURSOR_SWORD));
                currentCursor = "sword";
            }
        } catch (Exception e) {
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


}


