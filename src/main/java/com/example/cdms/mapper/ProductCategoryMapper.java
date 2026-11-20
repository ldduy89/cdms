package com.example.cdms.mapper;

import org.mapstruct.Mapper;

import com.example.cdms.dto.ProductCategoryRequest;
import com.example.cdms.dto.ProductCategoryResponse;
import com.example.cdms.entity.ProductCategory;


@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {
    ProductCategoryResponse toResponse(ProductCategory productCategory);
    ProductCategory toEntity(ProductCategoryRequest request);
}
