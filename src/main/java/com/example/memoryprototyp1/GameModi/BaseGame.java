package com.example.memoryprototyp1.GameModi;

import com.example.memoryprototyp1.*;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.memoryprototyp1.Card.getBackOfCards;
import static com.example.memoryprototyp1.Music.playButtonSound;

public class BaseGame {



    protected FlowPane imagesFlowPane;
    protected int flowPaneSize;
    protected ArrayList<MemoryCard> cardsInGame;
    protected MemoryCard firstCard, secondCard;
    protected boolean cardsAreFlipped;
    protected boolean isInMotion;

    protected static int timeCardsAreReveledInMillSec = 1500;



    public BaseGame(int flowPaneSize, FlowPane imagesFlowPane) {
        this.flowPaneSize = flowPaneSize;
        this.imagesFlowPane = imagesFlowPane;
    }

    public boolean getCardsAreFlipped() {
        return cardsAreFlipped;
    }

    public ArrayList<MemoryCard> getCardsInGame() {
        return cardsInGame;
    }

    public void playTheGame(){
        play();
        initializeImageView();
    }

    public void playAgaing(){
        if (!isInMotion){
            play();
        }
    }


//    Methode damit Timer weiß wann Game zueende is
    public boolean gameFinished(){
        return cardsInGame.stream().allMatch(MemoryCard::getRevealed);
    }

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
                if ((!this.getCardsInGame().get((int) imageView.getUserData()).getRevealed()) && !cardsAreFlipped){
                    this.flipCard((int) imageView.getUserData());
                }
            });
        }
    }



    public void play(){
        firstCard = null;
        secondCard = null;
        CardDeck deck = new CardDeck();
        deck.shuffle();
        cardsInGame = new ArrayList<>();
        cardsAreFlipped = false;

        for (int i = 0; i < flowPaneSize / 2; i++) {
            Card topCardFromDeck = deck.giveTopCard();

            cardsInGame.add(new MemoryCard(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));
            cardsInGame.add(new MemoryCard(topCardFromDeck.getName(), topCardFromDeck.getFrontOfCards()));

        }
        Collections.shuffle(cardsInGame);
        System.out.println(cardsInGame);
        rotateAllCardsToBackSide();
    }



    public void flipCard(int cardPosition){

        cardsInGame.get(cardPosition).setRevealed(true);

        if (firstCard == null){
            firstCard = cardsInGame.get(cardPosition);
            rotate(cardPosition, cardsInGame.get(cardPosition).getFrontOfCards(), 0);
        } else {
            secondCard = cardsInGame.get(cardPosition);
            cardsAreFlipped = true;
            rotate(cardPosition, cardsInGame.get(cardPosition).getFrontOfCards(), 0);

            checkForMatch();
        }

    }

    public void checkForMatch(){

        if (firstCard.sameCardAs(secondCard)){
            System.out.println("same");
            playButtonSound();
            cardsAreFlipped = false;

            //hier noch Player update einfügen
            firstCard.setCorrectPair(true);
            secondCard.setCorrectPair(true);
        } else {
            rotateBack();
        }

        firstCard = null;
        secondCard = null;
        PauseTransition delay = new PauseTransition(Duration.millis(1500));
        if (cardsAreFlipped){
            delay.play();
        }
        delay.setOnFinished(delayEvent ->{
            cardsAreFlipped = false;});
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
        PauseTransition delay = new PauseTransition(Duration.millis(timeCardsAreReveledInMillSec));
        delay.play();
        delay.setOnFinished(setIsInMotionFalse -> {isInMotion = false;});
    }

    public boolean allCardsFlipped() {
        return cardsInGame.stream().allMatch(MemoryCard::getRevealed);
    }

    //the following Method has been copied from ChatGPT (https://chat.openai.com/ , 17.01.2024)
    public void skipToTwoPairsLeft() {
        System.out.println("Skip to two pairs left");
    }
}