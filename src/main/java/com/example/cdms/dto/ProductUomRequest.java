package com.example.cdms.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductUomRequest(@NotBlank String sku, @NotBlank String unitCode, Integer baseUnitQty, Double length,
		Double width, Double height, Double weight, java.math.BigDecimal costPrice, java.math.BigDecimal sellingPrice) {
	public ProductUomRequest {
		if (baseUnitQty == null) {
			baseUnitQty = 1;
		}
	}
}