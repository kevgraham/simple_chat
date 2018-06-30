package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

public class UserThread implements Runnable {
    Socket socket;
    ChatServer server;
    Thread thrd;
    PrintWriter writer;

    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;

        thrd = new Thread(this);
        thrd.start();
    }

    @Override
    public void run() {

        try {
            // create reader/writer
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            // get username
            String username = reader.readLine();
            server.addUser(username);

            // announce username
            server.broadcast(username + " has just joined.", this);

            // display currently connected users
            displayUsers();

            // listen for messages
            String message;
            do {
                message = reader.readLine();
                server.broadcast("[" + username + "]: " + message, this);

            } while (!message.equals("exit"));

            // end connection
            socket.close();
            server.removeUser(username, this);
            server.broadcast(username + " has just left.", this);

        } catch (IOException ex) {
            System.out.println("Connection error with user");
        }
    }

    /**
     * Sends the given message to the client
     * @param message
     */
    public void sendMessage(String message) {
        writer.println(message);
    }

    /**
     * Sends users connected to chatroom to the client
     */
    private void displayUsers() {
        Set<String> users = server.getUsers();

        if (users != null) {
            writer.println("Connected Users: " + users);
        }
    }

}
