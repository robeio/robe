package io.robe.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.CloseStatus;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


public abstract class WebSocket implements WebSocketListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocket.class);
    private static final ConcurrentHashMap<String, WebSocket> activeWebSockets = new ConcurrentHashMap<>();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    private String uuid;
    private Session currentSession;

    public abstract String onConnect(Session session);

    public abstract void onMessage(Session session, String message);

    public abstract void onMessage(Session session, byte[] bytes);

    public abstract void onClose(Session session, int status, String reason);

    public abstract void onError(Throwable error);

    @Override
    public void onWebSocketBinary(byte[] bytes, int i, int i1) {
        LOGGER.info("CLIENT: {} received byte message length: {}", currentSession.getRemoteAddress().toString(), bytes.length);
        onMessage(currentSession, bytes);
    }

    @Override
    public void onWebSocketText(String message) {
        LOGGER.info("CLIENT: {} received text message length: {}", currentSession.getRemoteAddress().toString(), message.length());
        onMessage(currentSession, message);
    }

    @Override
    public void onWebSocketClose(int i, String reason) {
        LOGGER.info("CLIENT: {} closed connection because of {}.", currentSession.getRemoteAddress().toString(), reason);
        activeWebSockets.remove(uuid);
        onClose(currentSession, i, reason);
        currentSession = null;
    }

    @Override
    public void onWebSocketConnect(Session session) {
        this.currentSession = session;
        LOGGER.info("CLIENT: {} connection received.", session.getRemoteAddress().toString());
        String uuid = onConnect(session);
        if (uuid == null || uuid.trim().isEmpty()) {
            try {
                session.getRemote().sendString(toJson(new Packet(Packet.Type.UNAUTHORISED, "", null)));
            } catch (IOException e) {
                e.printStackTrace();
                session.close(new CloseStatus(-1, "Can't reply connection UUID to client."));
            }
        } else {
            this.uuid = uuid;
            activeWebSockets.put(uuid, this);
            try {
                session.getRemote().sendString(toJson(new Packet(Packet.Type.CONNECTED, "", null)));
            } catch (IOException e) {
                e.printStackTrace();
                session.close(new CloseStatus(-1, "Can't reply connection UUID to client."));
            }

        }
    }

    @Override
    public void onWebSocketError(Throwable error) {
        LOGGER.info("Error occurred {}.", error.getMessage());
        onError(error);
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public static void sendText(Packet packet) throws IOException {
        WebSocket webSocket = activeWebSockets.get(packet.getReceiver());
        if (webSocket != null)
            webSocket.currentSession.getRemote().sendString(toJson(packet));
    }

    public static String toJson(Packet packet) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(packet);
    }
}