import io.javalin.Javalin;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;


import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

    private static final int PORT = 8080;
    private static Map<Session, String> users = new ConcurrentHashMap<>();
    private static int userCount = 0;

    public static void main(String[] args) {
        Javalin app = Javalin.create();
        app.port(PORT);
        app.enableStaticFiles("/");
        joinChat(app);
    }

    /**
     * Creates a websocket
     * @param app
     */
    private static void joinChat(Javalin app) {
        app
                .ws("/chat", socket -> {

                    socket.onConnect(session -> {
                        // get username
                        String username = "user" + ++userCount;

                        // add to sessions
                        users.put(session, username);

                        // announce to chatroom
                        broadcast("", (username + " has joined."));
                    });

                    socket.onClose((session, status, message) -> {
                        // get username
                        String username = users.get(session);

                        // add to sessions
                        users.remove(session);

                        // announce to chatroom
                        broadcast("", (username + " has left."));
                    });

                    socket.onMessage((session, message) -> {
                        // get username
                        String username = users.get(session);

                        // send to chatroom
                        broadcast(username, message);
                    });

                })
                .start();
    }

    /**
     * Broadcasts a message to the chatroom as JSON
     * @param sender of the message
     * @param message to send
     */
    private static void broadcast(String sender, String message) {

        users.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(
                        new JSONObject()
                                .put("sender", sender)
                                .put("message", message)
                                .put("users", users.values()).toString()
                );
            } catch (IOException ex) {
                System.out.println("error broadcasting message");
            }
        });

    }

}
