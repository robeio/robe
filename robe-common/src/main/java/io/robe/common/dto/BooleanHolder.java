package io.robe.common.dto;

/**
 * Created by kamilbukum on 30/01/2017.
 */
public class BooleanHolder {
    private boolean value;
    public BooleanHolder(boolean value){
        this.value = value;
    }

    public void set(boolean value) {
        this.value = value;
    }

    public boolean is() {
        return value;
    }
}
