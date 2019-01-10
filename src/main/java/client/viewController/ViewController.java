package client.viewController;

public abstract class ViewController {
    private ScreenController screenController;

    public void setup(ScreenController screenController) {
        this.screenController = screenController;
    }

    public abstract void processCommand(String command, String[] parameters);

    public ScreenController getScreenController() {
        return screenController;
    }

    public void setScreenController(ScreenController screenController) {
        this.screenController = screenController;
    }
}
