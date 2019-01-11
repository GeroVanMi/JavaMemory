package client.viewController;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainViewController extends ViewController {

    @FXML
    private Button gameButton;

    @FXML
    private Label usernameLabel;

    public void setup(ScreenController screenController) {
        this.setScreenController(screenController);
        usernameLabel.setText(this.getScreenController().getClient().getName());
    }

    @Override
    public void processCommand(String command, String[] parameters) {
        if(command.equals("LFGCONFIRM")) {
            Platform.runLater(() -> {
                gameButton.setText("Cancel Search");
                gameButton.setOnAction(this::handleButtonCancel);
            });
        } else if(command.equals("CANCELCONFIRM")) {
            Platform.runLater(() -> {
                gameButton.setText("Find game");
                gameButton.setOnAction(this::findGame);
            });
        }
    }

    public void handleButtonCancel(ActionEvent event) {
        this.getScreenController().getClient().sendCommand("CANCELLFG", "");
    }

    @FXML
    public void findGame(ActionEvent event) {
        this.getScreenController().getClient().sendCommand("LFG", "");
    }
}
