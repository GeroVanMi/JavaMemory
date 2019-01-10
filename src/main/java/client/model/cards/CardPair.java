package client.model.cards;

public class CardPair {
    private String title;
    private ImageCard imageCard;
    private TextCard textCard;

    public CardPair(String title, ImageCard imageCard, TextCard textCard) {
        this.title = title;
        this.imageCard = imageCard;
        this.textCard = textCard;
    }

    public String getTitle() {
        return title;
    }

    public ImageCard getImageCard() {
        return imageCard;
    }

    public TextCard getTextCard() {
        return textCard;
    }
}