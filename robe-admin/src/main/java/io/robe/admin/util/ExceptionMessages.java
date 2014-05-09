package io.robe.admin.util;

public enum ExceptionMessages {
    CantBeNull(" cannot be null."),
    NotExists(" not exists.");


    private final String msg;

    private ExceptionMessages(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
