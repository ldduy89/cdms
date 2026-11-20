package com.example.cdms.mapper;

import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductResponse;
import com.example.cdms.entity.Product;
import com.example.cdms.entity.ProductBarcode;
import com.example.cdms.entity.ProductBundle;
import com.example.cdms.entity.ProductBundleDetail;
import com.example.cdms.enums.SerialType;
import com.example.cdms.enums.WarehouseCategoryType;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-20T09:15:42+0700",
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
        if ( request.serialType() != null ) {
            product.serialType( SerialType.values()[ request.serialType() ] );
        }
        product.outboundType( request.outboundType() );
        if ( request.warehouseCategory() != null ) {
            product.warehouseCategory( WarehouseCategoryType.values()[ request.warehouseCategory() ] );
        }
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
        product.productBundles( productBundleItemRequestListToProductBundleList( request.productBundles() ) );
        product.barcodes( barcodeItemRequestListToProductBarcodeList( request.barcodes() ) );

        return product.build();
    }

    protected ProductBundleDetail productBundleDetailRequestToProductBundleDetail(ProductRequest.ProductBundleDetailRequest productBundleDetailRequest) {
        if ( productBundleDetailRequest == null ) {
            return null;
        }

        ProductBundleDetail.ProductBundleDetailBuilder productBundleDetail = ProductBundleDetail.builder();

        productBundleDetail.qty( productBundleDetailRequest.qty() );
        productBundleDetail.salePrice( productBundleDetailRequest.salePrice() );

        return productBundleDetail.build();
    }

    protected List<ProductBundleDetail> productBundleDetailRequestListToProductBundleDetailList(List<ProductRequest.ProductBundleDetailRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductBundleDetail> list1 = new ArrayList<ProductBundleDetail>( list.size() );
        for ( ProductRequest.ProductBundleDetailRequest productBundleDetailRequest : list ) {
            list1.add( productBundleDetailRequestToProductBundleDetail( productBundleDetailRequest ) );
        }

        return list1;
    }

    protected ProductBundle productBundleItemRequestToProductBundle(ProductRequest.ProductBundleItemRequest productBundleItemRequest) {
        if ( productBundleItemRequest == null ) {
            return null;
        }

        ProductBundle.ProductBundleBuilder productBundle = ProductBundle.builder();

        productBundle.availablePercentage( productBundleItemRequest.availablePercentage() );
        productBundle.bundleDetails( productBundleDetailRequestListToProductBundleDetailList( productBundleItemRequest.bundleDetails() ) );
        productBundle.qty( productBundleItemRequest.qty() );

        return productBundle.build();
    }

    protected List<ProductBundle> productBundleItemRequestListToProductBundleList(List<ProductRequest.ProductBundleItemRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductBundle> list1 = new ArrayList<ProductBundle>( list.size() );
        for ( ProductRequest.ProductBundleItemRequest productBundleItemRequest : list ) {
            list1.add( productBundleItemRequestToProductBundle( productBundleItemRequest ) );
        }

        return list1;
    }

    protected ProductBarcode barcodeItemRequestToProductBarcode(ProductRequest.BarcodeItemRequest barcodeItemRequest) {
        if ( barcodeItemRequest == null ) {
            return null;
        }

        ProductBarcode.ProductBarcodeBuilder productBarcode = ProductBarcode.builder();

        productBarcode.barcode( barcodeItemRequest.barcode() );
        productBarcode.name( barcodeItemRequest.name() );
        productBarcode.partnerSKU( barcodeItemRequest.partnerSKU() );
        productBarcode.refCode( barcodeItemRequest.refCode() );

        return productBarcode.build();
    }

    protected List<ProductBarcode> barcodeItemRequestListToProductBarcodeList(List<ProductRequest.BarcodeItemRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductBarcode> list1 = new ArrayList<ProductBarcode>( list.size() );
        for ( ProductRequest.BarcodeItemRequest barcodeItemRequest : list ) {
            list1.add( barcodeItemRequestToProductBarcode( barcodeItemRequest ) );
        }

        return list1;
    }
}
