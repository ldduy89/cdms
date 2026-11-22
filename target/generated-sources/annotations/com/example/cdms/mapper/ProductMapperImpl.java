package com.example.cdms.mapper;

import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductResponse;
import com.example.cdms.entity.Product;
import com.example.cdms.entity.ProductBarcode;
import com.example.cdms.entity.ProductBundle;
import com.example.cdms.entity.ProductBundleDetail;
import com.example.cdms.entity.ProductCategory;
import com.example.cdms.entity.ProductCategoryMapping;
import com.example.cdms.entity.ProductUnit;
import com.example.cdms.entity.ProductUom;
import com.example.cdms.enums.AssetType;
import com.example.cdms.enums.OutboundType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-22T12:20:03+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260826-1225, environment: Java 25.0.5 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductResponse.UnitResponse toUnit(ProductUom uom) {
        if ( uom == null ) {
            return null;
        }

        String unitCode = null;
        String unitName = null;
        Double length = null;
        Double width = null;
        Double height = null;
        Double weight = null;
        Boolean isStockConversionEnabled = null;

        unitCode = uomUnitCode( uom );
        unitName = uomUnitName( uom );
        length = uom.getLength();
        width = uom.getWidth();
        height = uom.getHeight();
        weight = uom.getWeight();
        isStockConversionEnabled = uom.getIsStockConversionEnabled();

        ProductResponse.UnitResponse unitResponse = new ProductResponse.UnitResponse( unitCode, unitName, length, width, height, weight, isStockConversionEnabled );

        return unitResponse;
    }

    @Override
    public ProductResponse.CategoryResponse toCategory(ProductCategoryMapping mapping) {
        if ( mapping == null ) {
            return null;
        }

        String categoryCode = null;
        String categoryName = null;

        categoryCode = mappingCategoryCode( mapping );
        categoryName = mappingCategoryName( mapping );

        ProductResponse.CategoryResponse categoryResponse = new ProductResponse.CategoryResponse( categoryCode, categoryName );

        return categoryResponse;
    }

    @Override
    public ProductResponse.ProductBundleResponse toBundle(ProductBundle bundle) {
        if ( bundle == null ) {
            return null;
        }

        String unitCode = null;
        String sku = null;
        Integer qty = null;
        Double availablePercentage = null;
        List<ProductResponse.BundleDetailResponse> bundleDetails = null;

        unitCode = bundleUnitCode( bundle );
        sku = bundleChildProductSku( bundle );
        qty = bundle.getQty();
        availablePercentage = bundle.getAvailablePercentage();
        bundleDetails = productBundleDetailListToBundleDetailResponseList( bundle.getBundleDetails() );

        ProductResponse.ProductBundleResponse productBundleResponse = new ProductResponse.ProductBundleResponse( sku, unitCode, qty, availablePercentage, bundleDetails );

        return productBundleResponse;
    }

    @Override
    public ProductResponse toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        String avatarURL = null;
        List<ProductResponse.UnitResponse> units = null;
        List<ProductResponse.CategoryResponse> categories = null;
        List<ProductResponse.ProductBundleResponse> productBundles = null;
        List<ProductResponse.ProductBarcodeResponse> productBarcodes = null;
        Long productId = null;
        String productName = null;
        String sku = null;
        String partnerSKU = null;
        OutboundType outboundType = null;
        Boolean isExpiryDate = null;
        Boolean isLotNumber = null;
        Boolean isTrackingSerial = null;
        Integer numDaysWarningExpDate = null;
        Integer numDaysExportBeforeExpDate = null;
        String packingNote = null;
        AssetType assetType = null;
        Boolean isPhysical = null;
        String color = null;
        String size = null;
        String description = null;
        Boolean isActive = null;
        Boolean isDiscreet = null;
        String hsCode = null;
        String countryOfOrigin = null;
        String refCode = null;

        avatarURL = product.getAvatarUrl();
        units = productUomListToUnitResponseList( product.getProductUOMs() );
        categories = productCategoryMappingListToCategoryResponseList( product.getProductCategories() );
        productBundles = productBundleListToProductBundleResponseList( product.getProductBundles() );
        productBarcodes = productBarcodeListToProductBarcodeResponseList( product.getBarcodes() );
        productId = product.getProductId();
        productName = product.getProductName();
        sku = product.getSku();
        partnerSKU = product.getPartnerSKU();
        outboundType = product.getOutboundType();
        isExpiryDate = product.getIsExpiryDate();
        isLotNumber = product.getIsLotNumber();
        isTrackingSerial = product.getIsTrackingSerial();
        numDaysWarningExpDate = product.getNumDaysWarningExpDate();
        numDaysExportBeforeExpDate = product.getNumDaysExportBeforeExpDate();
        packingNote = product.getPackingNote();
        assetType = product.getAssetType();
        isPhysical = product.getIsPhysical();
        color = product.getColor();
        size = product.getSize();
        description = product.getDescription();
        isActive = product.getIsActive();
        isDiscreet = product.getIsDiscreet();
        hsCode = product.getHsCode();
        countryOfOrigin = product.getCountryOfOrigin();
        refCode = product.getRefCode();

        Boolean isAttribute = null;
        String parentPartnerSKU = null;

        ProductResponse productResponse = new ProductResponse( productId, productName, sku, partnerSKU, outboundType, isExpiryDate, isAttribute, isLotNumber, isTrackingSerial, numDaysWarningExpDate, numDaysExportBeforeExpDate, packingNote, avatarURL, assetType, isPhysical, color, size, description, isActive, units, categories, productBundles, productBarcodes, isDiscreet, parentPartnerSKU, hsCode, countryOfOrigin, refCode );

        return productResponse;
    }

    @Override
    public Product toEntity(ProductRequest request) {
        if ( request == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.avatarUrl( request.avatarURL() );
        product.sku( request.sku() );
        product.partnerSKU( request.partnerSKU() );
        product.productName( request.productName() );
        product.description( request.description() );
        product.color( request.color() );
        product.size( request.size() );
        product.packingNote( request.packingNote() );
        product.hsCode( request.hsCode() );
        product.countryOfOrigin( request.countryOfOrigin() );
        product.productTaxCode( request.productTaxCode() );
        product.productTaxName( request.productTaxName() );
        product.taxRate( request.taxRate() );
        product.refCode( request.refCode() );
        product.assetType( request.assetType() );
        product.serialType( request.serialType() );
        product.outboundType( request.outboundType() );
        product.warehouseCategory( request.warehouseCategory() );
        product.isPhysical( request.isPhysical() );
        product.isBarCode( request.isBarCode() );
        product.isDiscreet( request.isDiscreet() );
        product.isIgnoreReceivedDate( request.isIgnoreReceivedDate() );
        product.isIgnoreIRId( request.isIgnoreIRId() );
        product.isTrackingSerial( request.isTrackingSerial() );
        product.isExpiryDate( request.isExpiryDate() );
        product.isLotNumber( request.isLotNumber() );
        product.minInboundShelfLife( request.minInboundShelfLife() );
        product.minOutboundShelfLife( request.minOutboundShelfLife() );
        product.numDaysWarningExpDate( request.numDaysWarningExpDate() );
        product.numDaysExportBeforeExpDate( request.numDaysExportBeforeExpDate() );
        product.productLife( request.productLife() );
        product.outboundLimitConditionCode( request.outboundLimitConditionCode() );
        product.expiredConditionCode( request.expiredConditionCode() );

        return product.build();
    }

    @Override
    public void updateEntity(ProductRequest request, Product product) {
        if ( request == null ) {
            return;
        }

        if ( request.avatarURL() != null ) {
            product.setAvatarUrl( request.avatarURL() );
        }
        if ( request.sku() != null ) {
            product.setSku( request.sku() );
        }
        if ( request.partnerSKU() != null ) {
            product.setPartnerSKU( request.partnerSKU() );
        }
        if ( request.productName() != null ) {
            product.setProductName( request.productName() );
        }
        if ( request.description() != null ) {
            product.setDescription( request.description() );
        }
        if ( request.color() != null ) {
            product.setColor( request.color() );
        }
        if ( request.size() != null ) {
            product.setSize( request.size() );
        }
        if ( request.packingNote() != null ) {
            product.setPackingNote( request.packingNote() );
        }
        if ( request.hsCode() != null ) {
            product.setHsCode( request.hsCode() );
        }
        if ( request.countryOfOrigin() != null ) {
            product.setCountryOfOrigin( request.countryOfOrigin() );
        }
        if ( request.productTaxCode() != null ) {
            product.setProductTaxCode( request.productTaxCode() );
        }
        if ( request.productTaxName() != null ) {
            product.setProductTaxName( request.productTaxName() );
        }
        if ( request.taxRate() != null ) {
            product.setTaxRate( request.taxRate() );
        }
        if ( request.refCode() != null ) {
            product.setRefCode( request.refCode() );
        }
        if ( request.assetType() != null ) {
            product.setAssetType( request.assetType() );
        }
        if ( request.serialType() != null ) {
            product.setSerialType( request.serialType() );
        }
        if ( request.outboundType() != null ) {
            product.setOutboundType( request.outboundType() );
        }
        if ( request.warehouseCategory() != null ) {
            product.setWarehouseCategory( request.warehouseCategory() );
        }
        if ( request.isPhysical() != null ) {
            product.setIsPhysical( request.isPhysical() );
        }
        if ( request.isBarCode() != null ) {
            product.setIsBarCode( request.isBarCode() );
        }
        if ( request.isDiscreet() != null ) {
            product.setIsDiscreet( request.isDiscreet() );
        }
        if ( request.isIgnoreReceivedDate() != null ) {
            product.setIsIgnoreReceivedDate( request.isIgnoreReceivedDate() );
        }
        if ( request.isIgnoreIRId() != null ) {
            product.setIsIgnoreIRId( request.isIgnoreIRId() );
        }
        if ( request.isTrackingSerial() != null ) {
            product.setIsTrackingSerial( request.isTrackingSerial() );
        }
        if ( request.isExpiryDate() != null ) {
            product.setIsExpiryDate( request.isExpiryDate() );
        }
        if ( request.isLotNumber() != null ) {
            product.setIsLotNumber( request.isLotNumber() );
        }
        if ( request.minInboundShelfLife() != null ) {
            product.setMinInboundShelfLife( request.minInboundShelfLife() );
        }
        if ( request.minOutboundShelfLife() != null ) {
            product.setMinOutboundShelfLife( request.minOutboundShelfLife() );
        }
        if ( request.numDaysWarningExpDate() != null ) {
            product.setNumDaysWarningExpDate( request.numDaysWarningExpDate() );
        }
        if ( request.numDaysExportBeforeExpDate() != null ) {
            product.setNumDaysExportBeforeExpDate( request.numDaysExportBeforeExpDate() );
        }
        if ( request.productLife() != null ) {
            product.setProductLife( request.productLife() );
        }
        if ( request.outboundLimitConditionCode() != null ) {
            product.setOutboundLimitConditionCode( request.outboundLimitConditionCode() );
        }
        if ( request.expiredConditionCode() != null ) {
            product.setExpiredConditionCode( request.expiredConditionCode() );
        }
    }

    private String uomUnitCode(ProductUom productUom) {
        ProductUnit unit = productUom.getUnit();
        if ( unit == null ) {
            return null;
        }
        return unit.getCode();
    }

    private String uomUnitName(ProductUom productUom) {
        ProductUnit unit = productUom.getUnit();
        if ( unit == null ) {
            return null;
        }
        return unit.getName();
    }

    private String mappingCategoryCode(ProductCategoryMapping productCategoryMapping) {
        ProductCategory category = productCategoryMapping.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getCode();
    }

    private String mappingCategoryName(ProductCategoryMapping productCategoryMapping) {
        ProductCategory category = productCategoryMapping.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getName();
    }

    private String bundleUnitCode(ProductBundle productBundle) {
        ProductUnit unit = productBundle.getUnit();
        if ( unit == null ) {
            return null;
        }
        return unit.getCode();
    }

    private String bundleChildProductSku(ProductBundle productBundle) {
        Product childProduct = productBundle.getChildProduct();
        if ( childProduct == null ) {
            return null;
        }
        return childProduct.getSku();
    }

    protected ProductResponse.BundleDetailResponse productBundleDetailToBundleDetailResponse(ProductBundleDetail productBundleDetail) {
        if ( productBundleDetail == null ) {
            return null;
        }

        Integer qty = null;
        BigDecimal salePrice = null;

        qty = productBundleDetail.getQty();
        salePrice = productBundleDetail.getSalePrice();

        ProductResponse.BundleDetailResponse bundleDetailResponse = new ProductResponse.BundleDetailResponse( qty, salePrice );

        return bundleDetailResponse;
    }

    protected List<ProductResponse.BundleDetailResponse> productBundleDetailListToBundleDetailResponseList(List<ProductBundleDetail> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductResponse.BundleDetailResponse> list1 = new ArrayList<ProductResponse.BundleDetailResponse>( list.size() );
        for ( ProductBundleDetail productBundleDetail : list ) {
            list1.add( productBundleDetailToBundleDetailResponse( productBundleDetail ) );
        }

        return list1;
    }

    protected List<ProductResponse.UnitResponse> productUomListToUnitResponseList(List<ProductUom> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductResponse.UnitResponse> list1 = new ArrayList<ProductResponse.UnitResponse>( list.size() );
        for ( ProductUom productUom : list ) {
            list1.add( toUnit( productUom ) );
        }

        return list1;
    }

    protected List<ProductResponse.CategoryResponse> productCategoryMappingListToCategoryResponseList(List<ProductCategoryMapping> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductResponse.CategoryResponse> list1 = new ArrayList<ProductResponse.CategoryResponse>( list.size() );
        for ( ProductCategoryMapping productCategoryMapping : list ) {
            list1.add( toCategory( productCategoryMapping ) );
        }

        return list1;
    }

    protected List<ProductResponse.ProductBundleResponse> productBundleListToProductBundleResponseList(List<ProductBundle> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductResponse.ProductBundleResponse> list1 = new ArrayList<ProductResponse.ProductBundleResponse>( list.size() );
        for ( ProductBundle productBundle : list ) {
            list1.add( toBundle( productBundle ) );
        }

        return list1;
    }

    protected ProductResponse.ProductBarcodeResponse productBarcodeToProductBarcodeResponse(ProductBarcode productBarcode) {
        if ( productBarcode == null ) {
            return null;
        }

        String partnerSKU = null;

        partnerSKU = productBarcode.getPartnerSKU();

        Long productId = null;
        String productName = null;
        String sku = null;

        ProductResponse.ProductBarcodeResponse productBarcodeResponse = new ProductResponse.ProductBarcodeResponse( productId, productName, sku, partnerSKU );

        return productBarcodeResponse;
    }

    protected List<ProductResponse.ProductBarcodeResponse> productBarcodeListToProductBarcodeResponseList(List<ProductBarcode> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductResponse.ProductBarcodeResponse> list1 = new ArrayList<ProductResponse.ProductBarcodeResponse>( list.size() );
        for ( ProductBarcode productBarcode : list ) {
            list1.add( productBarcodeToProductBarcodeResponse( productBarcode ) );
        }

        return list1;
    }
}
