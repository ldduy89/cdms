package com.example.cdms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductResponse;
import com.example.cdms.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

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
}