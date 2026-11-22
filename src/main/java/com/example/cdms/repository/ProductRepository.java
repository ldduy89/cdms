package com.example.cdms.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cdms.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	boolean existsBySku(String sku);

	Optional<Product> findBySku(String sku);

	Optional<Product> findByPartnerSKU(String partnerSKU);

	List<Product> findAllBySkuIn(Collection<String> skus);
}
