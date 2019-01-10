package client.viewController;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginViewController extends ViewController {

    @FXML
    private TextField usernameField, ipField;

    @FXML
    public void handleButtonLogin() {
        if(!usernameField.getText().isEmpty()) {
            this.getScreenController().createClient(usernameField.getText(), ipField.getText());
            this.getScreenController().loadContent("/fxml/startpage.fxml");
        }
    }

    @Override
    public void processCommand(String command, String[] parameters) {

    }
}
