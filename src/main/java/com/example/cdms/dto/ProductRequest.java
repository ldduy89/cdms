package com.example.cdms.dto;

import java.math.BigDecimal;
import java.util.List;

import com.example.cdms.enums.AssetType;
import com.example.cdms.enums.OutboundType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
        @NotBlank String unitCode,
        String unitName,
        @NotEmpty @Valid List<ProductCategoryRequest> categories,
        @NotBlank String sku,
        String partnerSKU, 
        @NotBlank String productName,
        Integer serialType, 
        OutboundType outboundType, 
        Boolean isExpiryDate,
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
        @Valid List<ProductBundleItemRequest> productBundles,
        Integer minInboundShelfLife,
        Integer minOutboundShelfLife,
        Boolean isBarCode,
        Integer warehouseCategory, 
        Boolean isDiscreet,
        Boolean isIgnoreReceivedDate,
        Boolean isIgnoreIRId,
        String hsCode,
        String countryOfOrigin,
        String productTaxCode,
        String productTaxName,
        Double taxRate,
        String refCode,
        @NotEmpty @Valid List<ProductUnitItemRequest> productUnits,
        Integer productLife,
        String outboundLimitConditionCode,
        String expiredConditionCode,
        @Valid List<BarcodeItemRequest> barcodes
) {

    public ProductRequest {
        if (isExpiryDate == null) isExpiryDate = false;
        if (isLotNumber == null) isLotNumber = false;
        if (isTrackingSerial == null) isTrackingSerial = false;
        if (isBarCode == null) isBarCode = false;
        if (isDiscreet == null) isDiscreet = false;
    }

    public record ProductBundleItemRequest(
            String sku,
            String unitCode,
            Integer qty,
            Double availablePercentage,
            @Valid List<ProductBundleDetailRequest> bundleDetails
    ) {}

    public record ProductBundleDetailRequest(
            Integer qty,
            BigDecimal salePrice
    ) {}

    public record ProductUnitItemRequest(
            String unitCode,
            String unitName,
            Double weight,
            Double length,
            Double width,
            Double height, 
            @NotNull Integer baseUnitQty,
            BigDecimal costPrice,
            BigDecimal sellingPrice,
            @NotNull Boolean isBaseUnit,
            Boolean isStockConversionEnabled
    ) {}

    public record BarcodeItemRequest(
            String barcode,
            String partnerSKU,
            String name,
            String avatarURL,
            String refCode
    ) {}
}