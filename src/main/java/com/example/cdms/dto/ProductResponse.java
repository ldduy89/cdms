package com.example.cdms.dto;

import java.math.BigDecimal;
import java.util.List;

import com.example.cdms.enums.AssetType;
import com.example.cdms.enums.OutboundType;

public record ProductResponse(
        Long productId,
        String productName,
        String sku,
        String partnerSKU,
        OutboundType outboundType,
        Boolean isExpiryDate,
        Boolean isAttribute,
        Boolean isLotNumber,
        Boolean isTrackingSerial,
        Integer numDaysWarningExpDate,
        Integer numDaysExportBeforeExpDate,
        String packingNote,
        String avatarURL,
        AssetType assetType,
        Boolean isPhysical,
        String color,
        String size,
        String description,
        Boolean isActive,
        List<UnitResponse> units,
        List<CategoryResponse> categories,
        List<ProductBundleResponse> productBundles,
        List<ProductBarcodeResponse> productBarcodes,
        Boolean isDiscreet,
        String parentPartnerSKU,
        String hsCode,
        String countryOfOrigin,
        String refCode
) {

    public record UnitResponse(
            String unitCode,
            String unitName,
            Double length,
            Double width,
            Double height,
            Double weight,
            Boolean isStockConversionEnabled
    ) {}

    public record CategoryResponse(
            String categoryCode,
            String categoryName
    ) {}

    public record ProductBundleResponse(
            String sku,
            String unitCode,
            Integer qty,
            Double availablePercentage,
            List<BundleDetailResponse> bundleDetails
    ) {}

    public record BundleDetailResponse(
            Integer qty,
            BigDecimal salePrice
    ) {}

    public record ProductBarcodeResponse(
            Long productId,
            String productName,
            String sku,
            String partnerSKU
    ) {}
}