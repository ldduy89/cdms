package com.example.cdms.service;

import java.util.List;

import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductResponse;


public interface ProductService {
    List<ProductResponse> findAll();
    ProductResponse findByPartnerSKU(String partnerSKU);
    ProductResponse create(ProductRequest request);
    ProductResponse update(Long id, ProductRequest request);
}
