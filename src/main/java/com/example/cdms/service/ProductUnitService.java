package com.example.cdms.service;

import java.util.List;

import com.example.cdms.dto.ProductRequest;


public interface ProductUnitService {
	void ensureUnits(List<ProductRequest> requests);
}
