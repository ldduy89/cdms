package com.example.cdms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WarehouseCategoryType {
    TYPE_1(1),
    TYPE_2(2);

    private final int code;

    WarehouseCategoryType(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static WarehouseCategoryType fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (WarehouseCategoryType t : values()) {
            if (t.code == code) {
                return t;
            }
        }
        throw new IllegalArgumentException("warehouseCategory invalid: " + code);
    }
}