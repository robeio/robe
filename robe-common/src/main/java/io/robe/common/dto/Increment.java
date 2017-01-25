package io.robe.common.dto;

/**
 * Created by Fatih on 25/01/17.
 */
public class Increment {
    private int value;
    public Increment(int value) {
        this.value = value;
    }

    public int increment() {
        return this.value++;
    }
    public int get() {
        return value;
    }
}
