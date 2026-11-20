package com.example.cdms.service;

import java.util.List;

import com.example.cdms.dto.ProductCategoryResponse;
import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductResponse;
import com.example.cdms.dto.ProductUnitResponse;


public interface ProductService {
    List<ProductResponse> findAll();
    ProductResponse findByPartnerSKU(String partnerSKU);
    ProductResponse create(List<ProductRequest> request);
    ProductResponse update(Long id, ProductRequest request);
    List<ProductUnitResponse> findAllProductUnits();
    List<ProductCategoryResponse> findAllProductCategories();
}
