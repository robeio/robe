package io.robe.admin.util;

public enum ExceptionMessages {
    CANT_BE_NULL(" cannot be null."),
    NOT_EXISTS(" not exists.");


    private final String msg;

    private ExceptionMessages(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
