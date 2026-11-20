package com.example.cdms.mapper;

import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductResponse;
import com.example.cdms.entity.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-20T23:04:30+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260826-1225, environment: Java 25.0.5 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductResponse toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        String productName = null;

        productName = product.getProductName();

        ProductResponse productResponse = new ProductResponse( productName );

        return productResponse;
    }

    @Override
    public Product toEntity(ProductRequest request) {
        if ( request == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.avatarUrl( request.avatarURL() );
        product.hasSerial( request.isTrackingSerial() );
        product.hasExpiration( request.isExpiryDate() );
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
}
