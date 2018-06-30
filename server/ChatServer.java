package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {

    private int port;
    private Set<UserThread> userThreads = new HashSet<>();
    private Set<String> users = new HashSet<>();

    public ChatServer() {
        // default port
        this.port = 8080;
    }

    public static void main(String[] args) {
        // start the server
        ChatServer server = new ChatServer();
        server.start();
    }

    /**
     * Starts a server that listens for connections and creates a thread for each user that joins
     */
    private void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("server starting on port " + port + "...");

            // listen on port
            while (true) {

                // accept connection
                Socket socket = serverSocket.accept();
                System.out.println("New user connected.");

                // start new thread for user
                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
            }


        } catch (IOException ex) {
            System.out.println("Could not start server...");
        }
    }

    /**
     * Sends a message to all threads except the sender
     * @param message the message
     * @param exclude the sending thread
     */
    protected void broadcast(String message, UserThread exclude) {
        for (UserThread user : userThreads) {
            if (user != exclude) {
                user.sendMessage(message);
            }
        }
    }

    public void addUser(String username) {
        users.add(username);
    }

    public void removeUser(String username, UserThread thread) {
        boolean removed = users.remove(username);

        if (removed) {
            userThreads.remove(thread);
            System.out.println(username + " has disconnected.");
        }
    }

    public Set<String> getUsers() {
        return users;
    }
}
