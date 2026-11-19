package com.example.cdms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductResponse;
import com.example.cdms.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {@Override
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
	public ProductResponse create(ProductRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductResponse update(Long id, ProductRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
