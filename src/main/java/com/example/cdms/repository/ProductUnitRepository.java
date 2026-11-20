package com.example.cdms.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cdms.entity.ProductUnit;

public interface ProductUnitRepository extends JpaRepository<ProductUnit, Long>{
	Optional<ProductUnit> findByCode(String code);
	List<ProductUnit> findByCodeIn(Collection<String> codes);
}
