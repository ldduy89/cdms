package com.example.cdms.service;

import java.util.List;

import com.example.cdms.dto.ProductRequest;
import com.example.cdms.entity.ProductUnit;

public interface ProductUnitService {
	void ensureUnits(List<ProductRequest> requests);

	ProductUnit getByCode(String code);
}
