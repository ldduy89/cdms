package com.example.cdms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OutboundType {
    NOT_DEFINED("NotDefined"),
    FIFO("FIFO"),
    LIFO("LIFO"),
    FEFO("FEFO"),
    LEFO("LEFO");
    private final String value;

    OutboundType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static OutboundType fromValue(String value) {
        if (value == null) {
            return NOT_DEFINED;
        }
        for (OutboundType t : values()) {
            if (t.value.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("outboundType invalid: " + value);
    }
}
