package com.example.cdms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cdms.entity.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>{

}
