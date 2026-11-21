package com.example.cdms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AssetType {
	SINGLE("Single"), BUNDLE("Bundle"), MULTIPLE_BARCODES("MultipleBarcodes");

	private final String value;

	AssetType(String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	@JsonCreator
	public static AssetType fromValue(String value) {
		if (value == null) {
			return SINGLE;
		}
		for (AssetType t : values()) {
			if (t.value.equalsIgnoreCase(value)) {
				return t;
			}
		}
		throw new IllegalArgumentException("assetType invalid: " + value);
	}
}