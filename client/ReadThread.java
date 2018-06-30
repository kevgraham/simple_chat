package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadThread extends Thread {

    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;

    public ReadThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            System.out.println("Error with socket.");
        }
    }

    /**
     * Listens for messages from the server
     */
    public void run() {
        // listen for messages
        while (true) {
            try {
                // display message
                String response = reader.readLine();
                if (response != null) {
                    System.out.println("\n" + response);
                }
            } catch(IOException ex) {
                System.out.println("Error reading from server...");
                break;
            }

        }
    }
}
