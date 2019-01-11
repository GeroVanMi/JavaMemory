package client.viewController;

import client.model.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.Socket;

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

    public void createClient(String name, Socket socket) {
        client = new Client(name, this, socket);
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
        } else if(currentViewController instanceof MainViewController) {
            ((MainViewController) currentViewController).handleButtonCancel(null);
        }
    }

    public Client getClient() {
        return client;
    }
}
