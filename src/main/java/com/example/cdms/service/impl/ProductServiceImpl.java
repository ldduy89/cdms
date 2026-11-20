package com.example.cdms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cdms.dto.ProductCategoryResponse;
import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductResponse;
import com.example.cdms.dto.ProductUnitResponse;
import com.example.cdms.mapper.ProductCategoryMapper;
//import com.example.cdms.mapper.ProductMapper;
import com.example.cdms.mapper.ProductUnitMapper;
import com.example.cdms.repository.ProductCategoryRepository;
//import com.example.cdms.repository.ProductRepository;
import com.example.cdms.repository.ProductUnitRepository;
import com.example.cdms.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

//    private final ProductRepository productRepository;
    private final ProductUnitRepository productUnitRepository;
    private final ProductCategoryRepository productCategoryRepository;

//    private final ProductMapper productMapper;
    private final ProductUnitMapper productUnitMapper;
    private final ProductCategoryMapper productCategoryMapper;
	
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
	public ProductResponse create(List<ProductRequest> request) {
		
		return null;
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
