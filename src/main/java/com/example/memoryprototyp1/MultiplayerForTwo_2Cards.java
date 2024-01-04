package com.example.memoryprototyp1;

import javafx.scene.layout.FlowPane;

public class MultiplayerForTwo_2Cards extends BaseGame {
    public MultiplayerForTwo_2Cards(int flowPaneSize, FlowPane imagesFlowPane) {
        super(flowPaneSize, imagesFlowPane);
    }
    @Override
    public void flipCard(int cardPosition){

        cardsInGame.get(cardPosition).setRevealed(true);

        if (firstCard == null){
            firstCard = cardsInGame.get(cardPosition);
            rotate(cardPosition, cardsInGame.get(cardPosition).getImage(), 0);
        } else {
            secondCard = cardsInGame.get(cardPosition);
            bothCardsAreFlipped = false;
            rotate(cardPosition, cardsInGame.get(cardPosition).getImage(), 0);

            checkForMatch();
        }
    }
}
