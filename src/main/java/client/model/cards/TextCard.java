package client.model.cards;

import javafx.scene.control.Label;

public class TextCard extends Card {
    private Label content;

    public TextCard(String title, String text) {
        super(title);
        this.content = new Label(text);
        content.setWrapText(true);
        content.setId("cardText");
    }

    public Label getContent() {
        return content;
    }
}
