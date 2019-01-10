package client.model.cards;

import javafx.scene.image.ImageView;

public class ImageCard extends Card {
    private ImageView imageView;

    public ImageCard(String title, String path) {
        super(title);
        this.imageView = new ImageView("cards/images/" + path);
        imageView.setPreserveRatio(false);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
