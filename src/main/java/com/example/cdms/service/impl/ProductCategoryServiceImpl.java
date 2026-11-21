package com.example.cdms.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cdms.dto.ProductCategoryRequest;
import com.example.cdms.dto.ProductRequest;
import com.example.cdms.entity.ProductCategory;
import com.example.cdms.repository.ProductCategoryRepository;
import com.example.cdms.service.ProductCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {

	private final ProductCategoryRepository productCategoryRepository;

	@Override
	public void ensureCategories(List<ProductRequest> requests) {
		Map<String, String> names = new LinkedHashMap<>();
		for (ProductRequest r : requests) {

			for (ProductCategoryRequest c : r.categories()) {
				String code = c.code();
				String name = c.name();
				String current = names.get(code);

				if (code != null && !code.isBlank() && name != null && !name.isBlank() && current == null) {
					names.put(code, name);
				}
			}

		}

		Set<String> existing = productCategoryRepository.findByCodeIn(names.keySet()).stream()
				.map(ProductCategory::getCode).collect(Collectors.toSet());
		List<ProductCategory> toCreate = new ArrayList<>();

		for (Map.Entry<String, String> e : names.entrySet()) {
			if (existing.contains(e.getKey())) {
				continue;
			}
			toCreate.add(ProductCategory.builder().code(e.getKey()).name(e.getValue()).build());
		}
		if (!toCreate.isEmpty()) {
			productCategoryRepository.saveAllAndFlush(toCreate);
		}

	}

}
