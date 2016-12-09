package io.robe.common.dto;

/**
 * Default DTO for basic name value pair. Will be used for simple data transfers.
 */
public class BasicPair extends Pair<String, String> {

    /**
     * Default Constructor .
     */
    public BasicPair() {
    }

    /**
     * Simple constructor with parameters.
     *
     * @param name
     * @param value
     */
    public BasicPair(String name, String value) {
        this.setLeft(name);
        this.setRight(value);
    }

    /**
     * @return Current name
     */
    public String getName() {
        return this.getLeft();
    }

    /**
     * @return Current value
     */
    public String getValue() {
        return this.getRight();
    }
}
