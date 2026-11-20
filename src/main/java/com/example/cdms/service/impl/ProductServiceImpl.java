package com.example.cdms.service.impl;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cdms.dto.ProductCategoryResponse;
import com.example.cdms.dto.ProductCreateResponse;
import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductResponse;
import com.example.cdms.dto.ProductUnitResponse;
import com.example.cdms.mapper.ProductCategoryMapper;
import com.example.cdms.mapper.ProductUnitMapper;
import com.example.cdms.repository.ProductCategoryRepository;
import com.example.cdms.repository.ProductUnitRepository;
import com.example.cdms.repository.jdbc.ProductBulkInserter;
import com.example.cdms.service.ProductCategoryService;
import com.example.cdms.service.ProductService;
import com.example.cdms.service.ProductUnitService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductUnitRepository productUnitRepository;
    private final ProductCategoryRepository productCategoryRepository;

    private final ProductUnitMapper productUnitMapper;
    private final ProductCategoryMapper productCategoryMapper;

    private final ProductBulkInserter productBulkInserter;
    
    private final ProductUnitService productUnitService;
    private final ProductCategoryService productCategoryService;
	
	@Override
	public List<ProductResponse> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductResponse findByPartnerSKU(String partnerSKU) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductCreateResponse> create(List<ProductRequest> requests) {
		productUnitService.ensureUnits(requests);
		productCategoryService.ensureCategories(requests);
	    Map<String, Long> ids = productBulkInserter.insertProducts(requests);
	    productBulkInserter.insertChildren(requests, ids);
	    List<ProductCreateResponse> result = new ArrayList<>();
	    Set<String> added = new HashSet<>();
	    for (ProductRequest r : requests) {
	        Long productId = ids.get(r.sku());
	        if (productId == null || !added.add(r.sku())) {
	            continue;
	        }
	        String partnerSku = (r.partnerSKU() == null || r.partnerSKU().isBlank())
	                ? r.sku()
	                : r.partnerSKU();
	        result.add(new ProductCreateResponse(productId, r.sku(), partnerSku));
	    }
	    return result;
	}
	

	@Override
	public ProductResponse update(Long id, ProductRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductUnitResponse> findAllProductUnits() {
		return productUnitRepository.findAll().stream().map(productUnitMapper::toResponse).toList();
	}


	@Override
	public List<ProductCategoryResponse> findAllProductCategories() {
		return productCategoryRepository.findAll().stream().map(productCategoryMapper::toResponse).toList();
	}

}
