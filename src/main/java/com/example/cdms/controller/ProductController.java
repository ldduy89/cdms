package com.example.cdms.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.cdms.dto.ProductBySearchResponse;
import com.example.cdms.dto.ProductCategoryResponse;
import com.example.cdms.dto.ProductCreateResponse;
import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductResponse;
import com.example.cdms.dto.ProductUnitResponse;
import com.example.cdms.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;

	@GetMapping
	public List<ProductBySearchResponse> search(@RequestParam(name = "Keyword", required = false) String keyword,
			@RequestParam(name = "PartnerSKUs", required = false) List<String> partnerSKUs,
			@RequestParam(name = "SKUs", required = false) List<String> skus,
			@RequestParam(name = "PageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(name = "PageSize", defaultValue = "10") int pageSize) {
		return productService.search(keyword, partnerSKUs, skus, pageIndex, pageSize);
	}

	@GetMapping("/{parterSKU}")
	public ProductResponse getByPartnerSKU(@PathVariable String parterSKU) {
		return productService.findByPartnerSKU(parterSKU);
	}

	@PostMapping
	public ResponseEntity<List<ProductCreateResponse>> create(@Valid @RequestBody List<ProductRequest> request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
	}
	@PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<List<ProductCreateResponse>> importExcel(@RequestParam MultipartFile file) throws IOException {
		if (file.isEmpty()) {
			throw new IllegalArgumentException("File requied!");
		}
		String name = file.getOriginalFilename();
		if (name == null || !(name.endsWith(".xlsx") || name.endsWith(".xls"))) {
			throw new IllegalArgumentException("Requied: .xlsx, .xls");
		}
		return ResponseEntity.ok(productService.importFile(file));
	}

	@PutMapping("/{id}")
	public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
		return productService.update(id, request);
	}

	@GetMapping("/units")
	public List<ProductUnitResponse> getAllUnit() {
		return productService.findAllProductUnits();
	}

	@GetMapping("/categories")
	public List<ProductCategoryResponse> getAllCategories() {
		return productService.findAllProductCategories();
	}

}
