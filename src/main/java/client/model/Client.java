package client.model;

import client.viewController.ScreenController;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

    private String name;
    private Socket client;
    private PrintWriter writer;
    private ScreenController screenController;
    private String ip;

    public Client(String name, ScreenController screenController, Socket client) {
        this.name = name;
        this.screenController = screenController;
        this.client = client;
    }

    public void run() {
        try {
            // opening Stream to write and read
            InputStream inputStream = client.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            OutputStream outputStream = client.getOutputStream();
            writer = new PrintWriter(outputStream);
            writer.println(name);
            writer.flush();
            // -------------
            while (true) {
                // Commandhandeling
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] arguments = line.split("/");
                    // If the first split isn't "COMMAND" something is wrong
                    if (arguments[0].equals("COMMAND")) {
                        // Figure out the parameters and put them into a separate array
                        String[] parameters = new String[arguments.length - 2];
                        for (int i = 2; i < arguments.length; i++) {
                            parameters[i - 2] = arguments[i];
                        }
                        screenController.processCommand(arguments[1], parameters);
                    }

                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void sendCommand(String command, String parameter) {
        writer.println("COMMAND/" + command + "/" + parameter);
        writer.flush();
    }

    public String getName() {
        return name;
    }
}
