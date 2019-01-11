package client.viewController;

import client.model.XMLFileReader;
import client.model.cards.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class GameController extends ViewController {
    @FXML
    private GridPane playground;
    @FXML
    private Label labelMyName, myPoints, labelOpponentName, enemyPoints;

    private Field[][] fields;
    private CardPair[] pairs;
    private ArrayList<Card> cards;
    private Card firstCard, secondCard;
    private boolean firstCardPicked;
    private int myPointsCounter, enemyPointsCounter;
    private int rowCard1, colCard1, rowCard2, colCard2;

    @Override
    public void setup(ScreenController screenController) {
        super.setup(screenController);
        fields = new Field[6][6];
        pairs = new CardPair[18];
        cards = new ArrayList<>();
        myPointsCounter = 0;
        enemyPointsCounter = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Button cardButton = new Button();
                cardButton.setPrefSize(100, 100);
                cardButton.setOnAction(this::handleButtonClick);
                cardButton.getStyleClass().add("coveredCard");
                playground.add(cardButton, i, j);
            }
        }
        screenController.getClient().sendCommand("GAMEFOUNDCONFIRM", "");
    }

    @Override
    public void processCommand(String command, String[] parameters) {
        switch (command) {
            case "GAMEINIT":
                XMLFileReader xmlReader = new XMLFileReader("/cards/cardpairs.xml");
                this.pairs = xmlReader.getPairs();
                Random random = new Random(Integer.parseInt(parameters[0]));
                this.distributeCards(random);
                Platform.runLater(() -> {
                    if (parameters[1].equals(getScreenController().getClient().getName())) {
                        labelMyName.setText(parameters[1]);
                        labelOpponentName.setText(parameters[2]);
                    } else {
                        labelMyName.setText(parameters[2]);
                        labelOpponentName.setText(parameters[1]);
                    }
                });
                this.sendCommand("GAMEINITCONFIRM", "");
                break;
            case "YOURTURN":
                firstCardPicked = false;
                this.firstCard = null;
                this.secondCard = null;
                this.coverCards();
                for (Node child : playground.getChildren()) {
                    Platform.runLater(() -> {
                        child.getStyleClass().clear();
                        child.getStyleClass().add("coveredCard");
                        if (child instanceof Button) {
                            Button button = (Button) child;
                            button.setDisable(false);
                        }
                    });
                }
                break;
            case "NOTYOURTURN":
                this.firstCard = null;
                this.secondCard = null;
                for (Node child : playground.getChildren()) {
                    Platform.runLater(() -> {
                        child.getStyleClass().clear();
                        child.getStyleClass().add("disarmedCard");
                    });
                }
                this.disableButtons();
                this.coverCards();

                break;
            case "SHOWCARD":
                int row = Integer.parseInt(parameters[0]);
                int col = Integer.parseInt(parameters[1]);
                int pos = Integer.parseInt(parameters[2]);
                uncoverCard(row, col, pos);
                break;
            case "ENEMYSCORED":
                enemyPointsCounter++;
                Platform.runLater(() -> {
                    enemyPoints.setText("Points: " + enemyPointsCounter);
                    removeCard(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]));
                    removeCard(Integer.parseInt(parameters[2]), Integer.parseInt(parameters[3]));
                });
                break;
            case "ENEMYLEFT":
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Gegner verlässt das Spiel!");
                    alert.setContentText("Dein Gegner hat das Spiel verlassen!");
                    alert.showAndWait();
                    InfoBox infoBox = new InfoBox("Gegner verloren", "Dein Gegner hat das Spiel verlassen.");
                    infoBox.showAndWait();
                    sendCommand("GAMEOVERCONFIRM", "");
                    this.leaveGame();
                });
                break;
            case "GAMELOST":
                Platform.runLater(() -> {
                    Stage stage = new Stage();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Spiel verloren.");
                    alert.setContentText("Du hast das Spiel verloren.");
                    alert.showAndWait();
                    sendCommand("GAMEOVERCONFIRM", "");
                });
                break;
            case "GAMEWON":
                Platform.runLater(() -> {
                    InfoBox infoBox = new InfoBox("Sieg!", "Du hast das Spiel gewonnen!");
                    Label content = new Label("Du hast die Partie gegen " + labelOpponentName.getText() + " mit " +
                            myPointsCounter + " zu " + enemyPointsCounter + " gewonnen.");
                    content.setWrapText(true);
                    infoBox.add(content);
                    infoBox.showAndWait();
                    sendCommand("GAMEOVERCONFIRM", "");
                    getScreenController().loadContent("/fxml/startpage.fxml");
                });
                break;
        }
    }

    private void sendCommand(String command, String parameters) {
        this.getScreenController().getClient().sendCommand(command, parameters);
    }

    private void disableButtons() {
        for (Node child : playground.getChildren()) {
            Platform.runLater(() -> {
                if (child instanceof Button) {
                    Button button = (Button) child;
                    button.setDisable(true);
                }
            });
        }
    }

    private Card uncoverCard(int row, int col, int position) {
        Button button = (Button) playground.getChildren().get(position);
        Card pickedCard = fields[row][col].getCard();
        Platform.runLater(() -> {
            if (pickedCard instanceof TextCard) {
                button.getStyleClass().clear();
                button.setGraphic(((TextCard) fields[row][col].getCard()).getContent());
            } else if (pickedCard instanceof ImageCard) {
                button.getStyleClass().clear();
                button.setGraphic(((ImageCard) fields[row][col].getCard()).getImageView());
            }
        });
        return pickedCard;
    }

    private void coverCards() {
        for (Node node : playground.getChildren()) {
            Platform.runLater(() -> {
                Button button = (Button) node;
                button.setGraphic(null);
            });
        }
    }

    private void distributeCards(Random random) {
        for (CardPair cardPair : pairs) {
            cards.add(cardPair.getImageCard());
            cards.add(cardPair.getTextCard());
        }
        int counter = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                int nextCard = random.nextInt(cards.size());
                fields[i][j] = new Field((Button) playground.getChildren().get(counter), cards.get(nextCard));
                cards.remove(cards.get(nextCard));
                counter++;
            }
        }
    }

    public void leaveGame() {
        this.sendCommand("LEAVING", "");
        this.getScreenController().loadContent("/fxml/startpage.fxml");
    }

    private void removeCard(int row, int col) {
        fields[row][col].removeCard();
        Button button = fields[row][col].getButton();
        button.setDisable(true);
        button.setOnAction(this::disabledButton);
        button.getStyleClass().clear();
        button.setId("disabledButton");
        button.setGraphic(null);
    }

    private void disabledButton(ActionEvent event) {

    }


    private void handleButtonClick(ActionEvent event) {
        Button source = (Button) event.getSource();
        int counter = 0;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (source.equals(fields[i][j].getButton())) {
                    if (!firstCardPicked) {
                        firstCardPicked = true;
                        firstCard = uncoverCard(i, j, counter);
                        rowCard1 = i;
                        colCard1 = j;
                        this.sendCommand("FIRSTCARD", "" + i + "/" + j + "/" + counter);
                    } else {
                        secondCard = uncoverCard(i, j, counter);
                        rowCard2 = i;
                        colCard2 = j;
                        this.sendCommand("SECONDCARD", "" + i + "/" + j + "/" + counter);
                        this.disableButtons();
                    }
                    // Check if two cards have been selected
                    if(firstCard != null && secondCard != null) {
                        // Check if they match
                        if (firstCard.getTitle().equals(secondCard.getTitle())) {
                            myPointsCounter++;
                            myPoints.setText("Points: " + myPointsCounter);
                            removeCard(rowCard1, colCard1);
                            removeCard(rowCard2, colCard2);
                            this.sendCommand("POINTSCORED", "" + rowCard1 + "/" + colCard1 + "/" + rowCard2 + "/" + colCard2);
                        }
                    }
                }
                counter++;
            }
        }
    }
}
