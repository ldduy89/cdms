package com.example.cdms.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductResponse;
import com.example.cdms.dto.ProductResponse.CategoryResponse;
import com.example.cdms.dto.ProductResponse.ProductBundleResponse;
import com.example.cdms.dto.ProductResponse.UnitResponse;
import com.example.cdms.entity.Product;
import com.example.cdms.entity.ProductBundle;
import com.example.cdms.entity.ProductCategoryMapping;
import com.example.cdms.entity.ProductUom;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "unitCode", source = "unit.code")
    @Mapping(target = "unitName", source = "unit.name")
    UnitResponse toUnit(ProductUom uom);

    @Mapping(target = "categoryCode", source = "category.code")
    @Mapping(target = "categoryName", source = "category.name")
    CategoryResponse toCategory(ProductCategoryMapping mapping);

    @Mapping(target = "unitCode", source = "unit.code")
    @Mapping(target = "sku", source = "childProduct.sku")
    ProductBundleResponse toBundle(ProductBundle bundle);

    @Mapping(target = "isExpiryDate", source = "hasExpiration")
    @Mapping(target = "isTrackingSerial", source = "hasSerial")
    @Mapping(target = "avatarURL", source = "avatarUrl")
    @Mapping(target = "isAttribute", ignore = true)
    @Mapping(target = "parentPartnerSKU", ignore = true)
    @Mapping(target = "units", source = "productUOMs")
    @Mapping(target = "categories", source = "productCategories")
    @Mapping(target = "productBundles", source = "productBundles")
    @Mapping(target = "productBarcodes", source = "barcodes")
    ProductResponse toResponse(Product product);


    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "productUOMs", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    @Mapping(target = "productBundles", ignore = true)
    @Mapping(target = "barcodes", ignore = true)
    @Mapping(target = "avatarUrl", source = "avatarURL")
    @Mapping(target = "hasSerial", source = "isTrackingSerial")
    @Mapping(target = "hasExpiration", source = "isExpiryDate")
    Product toEntity(ProductRequest request);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "productUOMs", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    @Mapping(target = "productBundles", ignore = true)
    @Mapping(target = "barcodes", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "avatarUrl", source = "avatarURL")
    @Mapping(target = "hasSerial", source = "isTrackingSerial")
    @Mapping(target = "hasExpiration", source = "isExpiryDate")
    void updateEntity(ProductRequest request, @MappingTarget Product product);
}