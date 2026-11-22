package com.example.cdms.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.cdms.dto.ProductUomRequest;
import com.example.cdms.dto.ProductUomResponse;
import com.example.cdms.entity.ProductUom;


@Mapper(componentModel = "spring")
public interface ProductUomMapper {
	
	@Mapping(target = "sku", source = "product.sku")
	@Mapping(target = "unitCode", source = "unit.code")
    ProductUomResponse toResponse(ProductUom productUom);
    List<ProductUomResponse> toResponses(List<ProductUom> uoms);
    ProductUom toEntity(ProductUomRequest request);

}
