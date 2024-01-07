package com.example.memoryprototyp1.GameModi;

import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import static com.example.memoryprototyp1.Card.getBackOfCards;

public class Singleplayer_2Cards extends BaseGame{
    private int lastClickedCard;
    private ImageView displayImageView;
    public Singleplayer_2Cards(int flowPaneSize, FlowPane imagesFlowPane, ImageView displayImageView) {
        super(flowPaneSize, imagesFlowPane);
        this.displayImageView = displayImageView;
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
    public void checkForMatch(){

        if (firstCard.sameCardAs(secondCard)){
            System.out.println("same");
            rotateDisplayImageView(displayImageView ,cardsInGame.get(lastClickedCard).getFrontOfCards());
            CardsAreFlipped = false;

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
            CardsAreFlipped = false;});
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
