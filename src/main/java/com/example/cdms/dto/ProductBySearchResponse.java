package com.example.cdms.dto;

import java.util.List;

public record ProductBySearchResponse(Long productId, String productName, String sku, String partnerSKU,
		String assetType, Boolean isTrackingSerial, Boolean isExpiryDate, String color, String size, String description,
		Boolean isActive, List<String> units, List<ProductCategoryResponse> categories) {

}