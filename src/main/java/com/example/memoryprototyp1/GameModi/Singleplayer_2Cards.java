package com.example.memoryprototyp1.GameModi;

import com.example.memoryprototyp1.*;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.memoryprototyp1.Card.getBackOfCardsImage;
import static com.example.memoryprototyp1.MemoryController.isTimelineIsStopped;
import static com.example.memoryprototyp1.Music.MusicPlayer.playButtonSound;
import static com.example.memoryprototyp1.Score.deserializeScore;


public class Singleplayer_2Cards extends BaseGame {
    private int lastClickedCard;

    private AnchorPane highscoreAnchorPane;
    private TextField highscoreName;
    private Label placeFive;

    private Label placeFour;

    private Label placeOne;

    private Label placeThree;

    private Label placeTwo;
    private ImageView displayImageView;
    private Label yourScoreLabel;
    private static boolean delayStart = false;

    public Singleplayer_2Cards(int flowPaneSize, FlowPane imagesFlowPane, ImageView displayImageView, TextField highscoreName, Label placeFive, Label placeFour, Label placeOne, Label placeThree, Label placeTwo, AnchorPane highscoreAnchorPane, Label yourScoreLabel) {
        super(flowPaneSize, imagesFlowPane);
        this.displayImageView = displayImageView;
        this.highscoreName = highscoreName;
        this.placeFive = placeFive;
        this.placeFour = placeFour;
        this.placeOne = placeOne;
        this.placeThree = placeThree;
        this.placeTwo = placeTwo;
        this.highscoreAnchorPane = highscoreAnchorPane;
        this.yourScoreLabel = yourScoreLabel;
    }

    @Override
    public void initializeImageView() {


        for (int i = 0; i < imagesFlowPane.getChildren().size(); i++) {
            ImageView imageView = (ImageView) imagesFlowPane.getChildren().get(i);
            imageView.setImage(getBackOfCardsImage());
            imageView.setUserData(i);

            imageView.setOnMouseEntered(mouseEnteredEvent -> {
                if (!this.getCardsInGame().get((int) imageView.getUserData()).getRevealed()&& !isTimelineIsStopped()) {
                    this.setImageScale((int) imageView.getUserData(), 1.05);
                }
            });

            imageView.setOnMouseExited(mouseEnteredEvent -> {
                this.setImageScale((int) imageView.getUserData(), 1);
            });

            imageView.setOnMouseClicked(mouseEvent -> {
                if ((delayStart &&!this.getCardsInGame().get((int) imageView.getUserData()).getRevealed()) && !this.getCardsAreFlipped() && !isTimelineIsStopped()) {
                    this.flipCard((int) imageView.getUserData());
                    lastClickedCard = (int) imageView.getUserData();

                }
            });
        }
    }

    @Override
    public void play() {
        delayStart = false;
        PauseTransition initialDelay = new PauseTransition(Duration.seconds(1));
        initialDelay.setOnFinished(event -> {
            delayStart = true;
        });
        initialDelay.play();

        firstCard = null;
        secondCard = null;
        CardDeck deck = new CardDeck();
        deck.shuffle();
        cardsInGame = new ArrayList<>();
        cardsAreFlipped = false;

        for (int i = 0; i < flowPaneSize / 2; i++) {
            Card topCardFromDeck = deck.giveTopCard();

            cardsInGame.add(new Card(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));
            cardsInGame.add(new Card(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));

        }
        Collections.shuffle(cardsInGame);
        System.out.println(cardsInGame);
        rotateAllCardsToBackSide();
        rotateDisplayImageView(displayImageView, getBackOfCardsImage());
    }

    @Override
    public void checkForMatch() {



        if (firstCard.sameCardAs(secondCard)) {
            System.out.println("same");
            rotateDisplayImageView(displayImageView, cardsInGame.get(lastClickedCard).getFrontOfCards());
            playButtonSound();
            cardsAreFlipped = false;

            //hier noch Player update einfügen

        } else {
            rotateBack();
        }

        if (gameFinished()) {
            highscoreAnchorPane.setVisible(true);
            if (MemoryController.getSeconds() < 10){
                yourScoreLabel.setText("YOUR TIME: " + MemoryController.getMinutes() + ":0" + MemoryController.getSeconds());
            } else {
                yourScoreLabel.setText("YOUR TIME: " + MemoryController.getMinutes() + ":" + MemoryController.getSeconds());
            }

            Score.setScoreBoard(Score.deserializeScore());
            setScoreLabel(placeOne, 0);
            setScoreLabel(placeTwo, 1);
            setScoreLabel(placeThree, 2);
            setScoreLabel(placeFour, 3);
            setScoreLabel(placeFive,4);
        }

        firstCard = null;
        secondCard = null;
        PauseTransition delay = new PauseTransition(Duration.millis(1500));
        if (cardsAreFlipped){
            delay.play();
            delay.setOnFinished(delayEvent -> {
                cardsAreFlipped = false;
            });
        }

    }

    /**
     * rotiert die Große Display Karte neben dem Spielfeld
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

        rotateFirstHalf.setNode(imageView);
        rotateFirstHalf.setDuration(Duration.millis(200));
        rotateFirstHalf.setByAngle(90);
        rotateFirstHalf.setAxis(Rotate.Y_AXIS);
        rotateFirstHalf.play();
        rotateFirstHalf.setOnFinished(eventRotateSecondHalf -> {
            RotateTransition rotateSecondHalf = new RotateTransition();
            rotateSecondHalf.setNode(imageView);
            imageView.setImage(imageToBeShown);
            rotateSecondHalf.setDelay(Duration.seconds(0));
            rotateSecondHalf.setDuration(Duration.millis(200));
            rotateSecondHalf.setByAngle(-90);
            rotateSecondHalf.play();
        });
    }

    private void setScoreLabel(Label l, int pos) {
        Score[] scores = new Score[5];

        scores[pos] = deserializeScore()[pos];
        if (scores[pos] != null && scores[pos].getScoreSec() < 10) {
            l.setText(scores[pos].getScoreMin() + ":0" + scores[pos].getScoreSec() + " | " + scores[pos].getPlayerName());
        } else if (scores[pos] != null) {
            l.setText(scores[pos].getScoreMin() + ":" + scores[pos].getScoreSec() + " | " + scores[pos].getPlayerName());
        }

    }
}