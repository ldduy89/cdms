package com.example.cdms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SerialType {
    SERIAL(1),
    NON_SERIAL(2);

    private final int code;

    SerialType(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static SerialType fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (SerialType t : values()) {
            if (t.code == code) {
                return t;
            }
        }
        throw new IllegalArgumentException("serialType invalid: " + code);
    }
}