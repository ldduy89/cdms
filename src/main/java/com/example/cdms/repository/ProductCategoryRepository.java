package com.example.cdms.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cdms.entity.ProductCategory;
import com.example.cdms.entity.ProductUnit;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>{
	Optional<ProductCategory> findByCode(String code);
	List<ProductCategory> findByCodeIn(Collection<String> codes);

}
