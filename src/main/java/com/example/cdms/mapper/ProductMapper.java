package com.example.cdms.mapper;

import org.mapstruct.Mapper;

import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductResponse;
import com.example.cdms.entity.Product;


@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toResponse(Product product);
    Product toEntity(ProductRequest request);

}
