package io.robe.admin.dto;

/**
 * Default DTO for basic name value pair. Will be used for simple data transfers.
 */
public class BasicPair {
    private String name;
    private String value;

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
        this.name = name;
        this.value = value;
    }

    /**
     * @return Current name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Current value
     */
    public String getValue() {
        return value;
    }

    /**
     * String representation of pair
     *
     * @return name=value
     */
    @Override
    public String toString() {
        return name + "=" + value;
    }
}
