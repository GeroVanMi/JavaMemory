package client.viewController;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.Socket;

public class LoginViewController extends ViewController {

    @FXML
    private TextField usernameField, ipField;
    @FXML
    private Label errorName, errorIP;

    @FXML
    public void handleButtonLogin() {
        if(checkFields()) {
            try {
                this.getScreenController().createClient(
                        usernameField.getText(),
                        new Socket(ipField.getText(), 5555));
                this.getScreenController().loadContent("/fxml/startpage.fxml");
            } catch (IOException e) {
                InfoBox errorBox = new InfoBox("Fehler", "Server kann nicht gefunden werden!");
                Label text = new Label("Es wurde kein Server unter der IP \"" + ipField.getText() + "\" gefunden. " +
                        "Wurde der Server gestartet? Falls der Server auf dem lokalen Rechner läuft, geben Sie bitte " +
                        "\"localhost\" ein.");
                text.setWrapText(true);
                errorBox.add(text);
                this.ipField.setId("errorField");
                errorBox.showAndWait();
            }
        }
    }

    private boolean checkFields() {
        boolean check = true;
        if(usernameField.getText().isEmpty()) {
            usernameField.setId("errorField");
            errorName.setText("Bitte geben Sie einen Namen ein.");
            check = false;
        } else if(usernameField.getText().toCharArray().length > 15) {
            usernameField.setId("errorField");
            errorName.setText("Bitte geben Sie einen Namen unter 15 Zeichen ein.");
        } else {
            errorName.setText("");
            usernameField.setId("");
        }
        if(ipField.getText().isEmpty()) {
            ipField.setId("errorField");
            errorIP.setText("Bitte geben Sie eine IP ein. Falls der Server auf Ihrem Rechner läuft, können Sie auch " +
                    "\"localhost\" eingeben.");
            check = false;
        } else {
            errorIP.setText("");
            ipField.setId("");
        }
        return check;
    }

    @Override
    public void processCommand(String command, String[] parameters) {}
}
