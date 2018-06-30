package client;

import java.io.IOException;
import java.net.Socket;

public class ChatClient {

    private String hostname;
    private int port;
    private String username;

    public ChatClient(String hostname) {
        this.hostname = hostname;
        this.port = 8080;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please specify a hostname.");
            return;
        }

        // create new client
        ChatClient client = new ChatClient(args[0]);
        client.start();

    }

    /**
     * Creates read/write threads for chatroom
     */
    public void start() {
        try {

            // create connection
            Socket socket = new Socket(hostname, port);
            System.out.println("Connected to chatroom.");

            // create read/write threads
            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();

        } catch (IOException ex) {
            System.out.println("Could not connect to chatroom...");
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
