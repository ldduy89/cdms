package com.example.cdms.mapper;

import com.example.cdms.dto.ProductUnitRequest;
import com.example.cdms.dto.ProductUnitResponse;
import com.example.cdms.entity.ProductUnit;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-20T09:08:45+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260826-1225, environment: Java 25.0.5 (Eclipse Adoptium)"
)
@Component
public class ProductUnitMapperImpl implements ProductUnitMapper {

    @Override
    public ProductUnitResponse toResponse(ProductUnit productUnit) {
        if ( productUnit == null ) {
            return null;
        }

        String code = null;
        String name = null;

        code = productUnit.getCode();
        name = productUnit.getName();

        ProductUnitResponse productUnitResponse = new ProductUnitResponse( code, name );

        return productUnitResponse;
    }

    @Override
    public ProductUnit toEntity(ProductUnitRequest request) {
        if ( request == null ) {
            return null;
        }

        ProductUnit.ProductUnitBuilder productUnit = ProductUnit.builder();

        productUnit.code( request.code() );
        productUnit.name( request.name() );

        return productUnit.build();
    }
}
