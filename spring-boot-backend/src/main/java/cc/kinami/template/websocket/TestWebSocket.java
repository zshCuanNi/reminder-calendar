package cc.kinami.template.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ServerEndpoint(value = "/ws/{id}")
public class TestWebSocket {

    private static final CopyOnWriteArrayList<TestWebSocket> webSocketSet = new CopyOnWriteArrayList<>();

    private Session session;

    private String id;

    public static void sendAllMessage(String message) {
        for (TestWebSocket webSocket : webSocketSet) {
            try {
                webSocket.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        this.session = session;
        System.out.println(session.getPathParameters());
        webSocketSet.add(this);
        this.id = id;

        System.out.println("id: " + id);
        System.out.println(session.getRequestURI());
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        sendAllMessage("From " + id + ": " + message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}
