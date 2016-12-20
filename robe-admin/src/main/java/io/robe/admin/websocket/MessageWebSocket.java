package io.robe.admin.websocket;


import io.robe.websocket.RobeWebSocket;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.nio.ByteBuffer;

@RobeWebSocket(path = "chat")
public class MessageWebSocket extends AuthenticatedWebSocket {

    @Override
    public void onMessage(Session session, String message) {
        try {
            session.getRemote().sendString("Message: " + message.toUpperCase());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Session session, byte[] bytes) {
        try {
            session.getRemote().sendBytes(ByteBuffer.wrap(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(Session session, int status, String reason) {
        try {
            session.getRemote().sendString("closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable error) {
        error.printStackTrace();

    }

}
