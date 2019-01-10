package client.viewController;

import client.model.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ScreenController {
    // To be replaced with user object
    private Client client;
    private ViewController currentViewController;

    @FXML
    private AnchorPane container;

    public void setup() {
        loadContent("/fxml/login.fxml");
    }

    public void loadContent(String path) {
        try {
            container.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent content = loader.load();
            content.requestFocus();
            ViewController viewController = loader.getController();
            viewController.setup(this);
            currentViewController = viewController;
            container.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createClient(String name, String ip) {
        client = new Client(name, this, ip);
        Thread inputThread = new Thread(client);
        inputThread.start();
    }

    public void processCommand(String command, String[] parameters) {
        if(command.equals("FOUNDGAME")) {
            Platform.runLater(() -> loadContent("/fxml/game.fxml"));
        } else {
            currentViewController.processCommand(command, parameters);
        }
    }

    public void exit() {
        if(currentViewController instanceof GameController) {
            ((GameController) currentViewController).leaveGame();
        }
    }

    public Client getClient() {
        return client;
    }
}