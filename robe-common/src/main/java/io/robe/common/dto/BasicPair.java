package io.robe.common.dto;

import com.google.common.base.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicPair basicPair = (BasicPair) o;
        return Objects.equal(name, basicPair.name) &&
                Objects.equal(value, basicPair.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, value);
    }

}
