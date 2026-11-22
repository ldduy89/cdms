package com.example.cdms.mapper;

import com.example.cdms.dto.ProductCategoryRequest;
import com.example.cdms.dto.ProductCategoryResponse;
import com.example.cdms.entity.ProductCategory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-21T23:33:08+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260826-1225, environment: Java 25.0.5 (Eclipse Adoptium)"
)
@Component
public class ProductCategoryMapperImpl implements ProductCategoryMapper {

    @Override
    public ProductCategoryResponse toResponse(ProductCategory productCategory) {
        if ( productCategory == null ) {
            return null;
        }

        String categoryCode = null;
        String categoryName = null;

        ProductCategoryResponse productCategoryResponse = new ProductCategoryResponse( categoryCode, categoryName );

        return productCategoryResponse;
    }

    @Override
    public ProductCategory toEntity(ProductCategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        ProductCategory.ProductCategoryBuilder productCategory = ProductCategory.builder();

        productCategory.code( request.code() );
        productCategory.name( request.name() );

        return productCategory.build();
    }
}
