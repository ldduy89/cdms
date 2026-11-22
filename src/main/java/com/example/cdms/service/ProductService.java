package com.example.cdms.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.cdms.dto.ProductBySearchResponse;
import com.example.cdms.dto.ProductCategoryResponse;
import com.example.cdms.dto.ProductCreateResponse;
import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductResponse;
import com.example.cdms.dto.ProductUnitResponse;
import com.example.cdms.dto.ProductUomRequest;
import com.example.cdms.dto.ProductUomResponse;

public interface ProductService {
	List<ProductBySearchResponse> search(String keyword, List<String> partnerSkus, List<String> skus, int pageIndex,
			int pageSize);

	ProductResponse findByPartnerSKU(String partnerSKU);

	List<ProductCreateResponse> create(List<ProductRequest> request);
	
	List<ProductUomResponse> createUom(List<ProductUomRequest> request);

	ProductResponse update(Long id, ProductRequest request);

	List<ProductUnitResponse> findAllProductUnits();

	List<ProductCategoryResponse> findAllProductCategories();

	List<ProductCreateResponse> importFile(MultipartFile file) throws IOException;

}
