package com.example.cdms.enums;

public enum AssetType {
	SINGLE(1),
    BUNDLE(2),
    MULTIPLE_BARCODES(3);

    private final int code;

    AssetType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
