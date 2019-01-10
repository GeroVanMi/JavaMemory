package client.model.cards;

import client.model.cards.Card;
import javafx.scene.control.Button;

public class Field {
    private Button button;
    private Card card;
    private boolean hasCard;

    public Field(Button button, Card card) {
        this.button = button;
        this.card = card;
        this.hasCard = true;
    }

    public Button getButton() {
        return button;
    }

    public Card getCard() {
        return card;
    }

    public boolean hasCard() {
        return hasCard;
    }

    public void removeCard() {
        this.hasCard = false;
    }
}