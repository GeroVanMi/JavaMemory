package client.viewController;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.net.URISyntaxException;

public class InfoBox {

    private Stage stage;
    private VBox content;

    public InfoBox(String paneTitle, String title) {
        VBox root = new VBox();
        root.setPrefSize(400, 200);
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("arial", FontWeight.BOLD, 18));
        titleLabel.setPadding(new Insets(15, 15, 15, 15));

        titleLabel.setPrefWidth(400);
        titleLabel.setWrapText(true);
        root.getChildren().add(titleLabel);
        root.setId("window");

        content = new VBox();
        content.setPrefSize(400, 100);
        content.setPadding(new Insets(15, 15, 15, 15));

        root.getChildren().add(content);


        Button button = new Button("OK");
        button.setOnAction(this::handleOkButton);
        button.setId("okButton");
        button.setFont(Font.font(16));
        HBox bottom = new HBox(button);
        HBox.setMargin(button, new Insets(10, 10, 10, 10));
        bottom.setAlignment(Pos.CENTER_RIGHT);
        bottom.setPrefHeight(50);
        root.getChildren().add(bottom);

        Scene scene = new Scene(root);
        try {
            scene.getStylesheets().add(InfoBox.class.getResource("/stylesheets/infoBox.css").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(paneTitle);
    }

    public void add(Node node) {
        content.getChildren().add(node);
    }

    public void handleOkButton(ActionEvent event) {
        stage.close();
    }

    public void showAndWait() {
        stage.showAndWait();
    }
}
