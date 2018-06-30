package client;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class WriteThread extends Thread {

    Socket socket;
    ChatClient client;
    PrintWriter writer;

    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("Error with socket.");
        }

    }

    /**
     * Writes messages to the server
     */
    public void run() {
        // get username
        Console console = System.console();
        String username = console.readLine("\nEnter username: ");

        // write to server
        client.setUsername(username);

        // get message
        String text;

        do {
            // read input
            text = console.readLine("[" + username + "]: ");

            // write to server
            writer.println(text);
        } while (text.equals("exit"));


    }
}
