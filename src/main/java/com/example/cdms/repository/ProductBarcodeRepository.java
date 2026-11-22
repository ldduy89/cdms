package com.example.cdms.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.cdms.entity.ProductBarcode;

public interface ProductBarcodeRepository extends JpaRepository<ProductBarcode, Long> {
	Optional<ProductBarcode> findByBarcode(String barcode);

	List<ProductBarcode> findByBarcodeIn(Collection<String> barcodes);
	
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("delete from ProductBarcode b where b.product.productId = :productId")
	void deleteByProductId(@Param("productId") Long productId);
}
