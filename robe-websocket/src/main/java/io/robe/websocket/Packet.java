package io.robe.websocket;

public class Packet {

    private Type type;
    private String receiver;
    private Object payload;

    public Packet() {
    }

    public Packet(Type type, String receiver, Object payload) {
        this.type = type;
        this.receiver = receiver;
        this.payload = payload;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public static enum Type {
        UNAUTHORISED,
        CONNECTED,
        MESSAGE,
        NOTIFICATION
    }
}