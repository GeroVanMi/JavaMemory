package client;

import client.viewController.ScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Memory extends Application {

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/container.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ScreenController screenController = loader.getController();
        screenController.setup();
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Memory");
        primaryStage.setOnCloseRequest(event -> {
            screenController.exit();
            synchronized (this) {
                System.exit(0);
            }
        });
        primaryStage.show();
    }
}
