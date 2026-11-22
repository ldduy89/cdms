package com.example.cdms.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductRequest.ProductUnitItemRequest;
import com.example.cdms.entity.ProductUnit;
import com.example.cdms.repository.ProductUnitRepository;
import com.example.cdms.service.ProductUnitService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductUnitServiceImpl implements ProductUnitService {

	private final ProductUnitRepository productUnitRepository;

	@Override
	public void ensureUnits(List<ProductRequest> requests) {
		Map<String, String> names = new LinkedHashMap<>();

		for (ProductRequest r : requests) {
			collectUnit(names, r.unitCode(), r.unitName());
			if (r.productUnits() != null) {
				for (ProductUnitItemRequest u : r.productUnits()) {
					collectUnit(names, u.unitCode(), u.unitName());
				}
			}
		}

		if (names.isEmpty()) {
			return;
		}

		Set<String> existing = productUnitRepository.findByCodeIn(names.keySet()).stream()
				.map(ProductUnit::getCode)
				.collect(Collectors.toSet());

		List<ProductUnit> toCreate = new ArrayList<>();
		for (Map.Entry<String, String> e : names.entrySet()) {
			if (existing.contains(e.getKey())) {
				continue;
			}
			if (e.getValue() == null) {
				throw new IllegalArgumentException("unitName is require: " + e.getKey());
			}
			toCreate.add(ProductUnit.builder().code(e.getKey()).name(e.getValue()).build());
		}

		if (!toCreate.isEmpty()) {
			productUnitRepository.saveAllAndFlush(toCreate);
		}
	}

	private void collectUnit(Map<String, String> names, String code, String name) {
		if (code == null || code.isBlank()) {
			return;
		}
		String cleanName = (name == null || name.isBlank()) ? null : name;
		if (!names.containsKey(code) || names.get(code) == null) {
			names.put(code, cleanName);
		}
	}

	@Override
	public ProductUnit getByCode(String code) {
		return productUnitRepository.findByCode(code)
				.orElseThrow(() -> new IllegalArgumentException("Not Found unitCode: " + code));
	}

}
