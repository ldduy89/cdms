package com.example.cdms.service;

import java.util.List;

import com.example.cdms.dto.ProductRequest;

public interface ProductCategoryService {
	void ensureCategories(List<ProductRequest> requests);
}
