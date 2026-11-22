package com.example.cdms.mapper;

import org.mapstruct.Mapper;

import com.example.cdms.dto.ProductUnitRequest;
import com.example.cdms.dto.ProductUnitResponse;
import com.example.cdms.entity.ProductUnit;


@Mapper(componentModel = "spring")
public interface ProductUnitMapper {
    ProductUnitResponse toResponse(ProductUnit productUnit);
    ProductUnit toEntity(ProductUnitRequest request);

}
