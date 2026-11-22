package com.example.cdms.dto;

public record ProductUomResponse(String sku, String unitCode, Integer baseUnitQty, Double length, Double width,
		Double height, Double weight, java.math.BigDecimal costPrice, java.math.BigDecimal sellingPrice) {

}
