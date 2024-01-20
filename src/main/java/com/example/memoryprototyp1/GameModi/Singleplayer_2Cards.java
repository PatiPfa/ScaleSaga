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

import static com.example.memoryprototyp1.Card.getBackOfCards;
import static com.example.memoryprototyp1.Music.MusicPlayer.playButtonSound;


public class Singleplayer_2Cards extends BaseGame{
    private int lastClickedCard;

    private AnchorPane highscoreAnchorPane;
    private TextField highscoreName;
    private Label placeFive;

    private Label placeFour;

    private Label placeOne;

    private Label placeThree;

    private Label placeTwo;
    private ImageView displayImageView;
    public Singleplayer_2Cards(int flowPaneSize, FlowPane imagesFlowPane, ImageView displayImageView, TextField highscoreName, Label placeFive, Label placeFour, Label placeOne, Label placeThree, Label placeTwo, AnchorPane highscoreAnchorPane) {
        super(flowPaneSize, imagesFlowPane);
        this.displayImageView = displayImageView;
        this.highscoreName =  highscoreName;
        this.placeFive = placeFive;
        this.placeFour = placeFour;
        this.placeOne = placeOne;
        this.placeThree = placeThree;
        this.placeTwo = placeTwo;
        this.highscoreAnchorPane = highscoreAnchorPane;
    }
    @Override
    public void initializeImageView() {


        for (int i = 0; i < imagesFlowPane.getChildren().size(); i++) {
            ImageView imageView = (ImageView) imagesFlowPane.getChildren().get(i);
            imageView.setImage(getBackOfCards());
            imageView.setUserData(i);

            imageView.setOnMouseEntered(mouseEnteredEvent ->{
                if (!this.getCardsInGame().get((int) imageView.getUserData()).getRevealed()){
                    this.setImageScale((int) imageView.getUserData(), 1.05);
                }
            });

            imageView.setOnMouseExited(mouseEnteredEvent ->{
                this.setImageScale((int) imageView.getUserData(), 1);
            });

            imageView.setOnMouseClicked(mouseEvent -> {
                if ((!this.getCardsInGame().get((int) imageView.getUserData()).getRevealed()) && !this.getCardsAreFlipped()){
                    this.flipCard((int) imageView.getUserData());
                    lastClickedCard = (int) imageView.getUserData();
                }
            });
        }
    }
    @Override
    public void play(){
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
        rotateDisplayImageView(displayImageView, getBackOfCards());
    }

    @Override
    public void checkForMatch(){

        if (firstCard.sameCardAs(secondCard)){
            System.out.println("same");
            rotateDisplayImageView(displayImageView ,cardsInGame.get(lastClickedCard).getFrontOfCards());
            playButtonSound();
            cardsAreFlipped = false;

            //hier noch Player update einfügen
            firstCard.setCorrectPair(true);
            secondCard.setCorrectPair(true);
        } else {
            rotateBack();
        }

        if (gameFinished()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Winner!");
            alert.setHeaderText(null);
            alert.setContentText("You have won :D");
            alert.showAndWait();
        }

        firstCard = null;
        secondCard = null;
        PauseTransition delay = new PauseTransition(Duration.millis(1500));
        delay.play();
        delay.setOnFinished(delayEvent ->{
            cardsAreFlipped = false;});
    }

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


}
