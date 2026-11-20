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
			String unitCode = r.unitCode();
			String unitName = r.unitName();
			String current = names.get(unitCode);
			
			if (unitCode != null && !unitCode.isBlank() && unitName != null && !unitName.isBlank() && current == null) {
				names.put(unitCode, unitName);
	        }
	    }
		
		Set<String> existing = productUnitRepository.findByCodeIn(names.keySet()).stream()
		            .map(ProductUnit::getCode)
		            .collect(Collectors.toSet());
		List<ProductUnit> toCreate = new ArrayList<>();
		
		for (Map.Entry<String, String> e : names.entrySet()) {
	        if (existing.contains(e.getKey())) {
	            continue;
	        }
	        toCreate.add(ProductUnit.builder().code(e.getKey()).name(e.getValue()).build());
	    }
		if (!toCreate.isEmpty()) {
	        productUnitRepository.saveAllAndFlush(toCreate);
	    }
		
	}

}
