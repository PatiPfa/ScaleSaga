package com.example.memoryprototyp1;

import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import static com.example.memoryprototyp1.Card.getBackOfCards;

public class BaseGame {



    protected FlowPane imagesFlowPane;
    protected int flowPaneSize;
    protected ArrayList<MemoryCard> cardsInGame;
    protected MemoryCard firstCard, secondCard;
    protected boolean bothCardsAreFlipped;

    public boolean getBothCardsAreFlipped() {
        return bothCardsAreFlipped;
    }

    private boolean isInMotion;


    public BaseGame(int flowPaneSize, FlowPane imagesFlowPane) {
        this.flowPaneSize = flowPaneSize;
        this.imagesFlowPane = imagesFlowPane;
    }

    public ArrayList<MemoryCard> getCardsInGame() {
        return cardsInGame;
    }

    public void playAgaing(){
        if (!isInMotion){
            play();
        }
    }

    public void play(){
        firstCard = null;
        secondCard = null;
        CardDeck deck = new CardDeck();
        deck.shuffle();
        cardsInGame = new ArrayList<>();
        bothCardsAreFlipped = false;


        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        player1.setColor(Color.RED);
        player2.setColor(Color.BLUE);

        for (int i = 0; i < flowPaneSize / 2; i++) {
            Card topCardFromDeck = deck.giveTopCard();

            cardsInGame.add(new MemoryCard(topCardFromDeck.getName()));
            cardsInGame.add(new MemoryCard(topCardFromDeck.getName()));

        }
        Collections.shuffle(cardsInGame);
        System.out.println(cardsInGame);
        rotateAllCardsToBackSide();
    }



    public void flipCard(int cardPosition){

        cardsInGame.get(cardPosition).setRevealed(true);

        if (firstCard == null){
            firstCard = cardsInGame.get(cardPosition);
            rotate(cardPosition, cardsInGame.get(cardPosition).getImage(), 0);
        } else {
            secondCard = cardsInGame.get(cardPosition);
            bothCardsAreFlipped = true;
            rotate(cardPosition, cardsInGame.get(cardPosition).getImage(), 0);

            checkForMatch();
        }

    }

    public void checkForMatch(){

        if (firstCard.sameCardAs(secondCard)){
            System.out.println("same");
            bothCardsAreFlipped = false;

            //hier noch Player update einfügen
            firstCard.setCorrectPair(true);
            secondCard.setCorrectPair(true);
        } else {
            rotateBack();
        }

        firstCard = null;
        secondCard = null;
        PauseTransition delay = new PauseTransition(Duration.millis(1500));
        delay.play();
        delay.setOnFinished(delayEvent ->{
            bothCardsAreFlipped = false;});
    }

    public void rotate(int cardPosition, Image imageToBeShown, double firstDelay){

        ImageView imageView = (ImageView) imagesFlowPane.getChildren().get(cardPosition);

        TranslateTransition translate = new TranslateTransition();
        RotateTransition rotateFirstHalf = new RotateTransition();

        setImageScale(cardPosition, 1);

        translate.setNode(imageView);
        translate.setByY(-10);
        translate.setDuration(Duration.millis(200));
        translate.setDelay(Duration.seconds(firstDelay));
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
        rotateFirstHalf.setDelay(Duration.seconds(firstDelay));
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
    public void rotateBack(){

        int indexFirstCard = cardsInGame.indexOf(firstCard);
        int indexSecondCard = cardsInGame.indexOf(secondCard);
        PauseTransition delay = new PauseTransition(Duration.millis(1500)); //<- time how long the cards are revealed
        delay.play();
        delay.setOnFinished(delayEvent ->{
            rotate(indexFirstCard, getBackOfCards(), 0);
            rotate(indexSecondCard, getBackOfCards(), 0);
            PauseTransition delay2 = new PauseTransition(Duration.millis(485));//<- after delay setRevealed is set false, this prevents card flip bugs
            delay2.play();
            delay2.setOnFinished(cardsAreFlippedBack ->{
                cardsInGame.get(indexFirstCard).setRevealed(false);
                cardsInGame.get(indexSecondCard).setRevealed(false);
            });
        });
    }

    public void setImageScale(int cardPosition, double newScale){
        ImageView imageView = (ImageView) imagesFlowPane.getChildren().get(cardPosition);
        imageView.setScaleX(newScale);
        imageView.setScaleY(newScale);
    }

    public void rotateAllCardsToBackSide(){

        if (!isInMotion){
            for (int i = 0; i < imagesFlowPane.getChildren().size(); i++) {
                rotate(i, getBackOfCards(),0);

            }
        }
        isInMotion = true;
        PauseTransition delay = new PauseTransition(Duration.millis(1000));
        delay.play();
        delay.setOnFinished(setIsInMotionFalse -> {isInMotion = false;});
    }

}