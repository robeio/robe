package io.robe.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Default DTO for basic name value pair. Will be used for simple data transfers.
 */
@JsonIgnoreProperties(value = {"left", "right"})
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
    @JsonProperty("name")
    public String getName() {
        return this.getLeft();
    }

    /**
     * @return Current value
     */
    @JsonProperty("value")
    public String getValue() {
        return this.getRight();
    }
}
