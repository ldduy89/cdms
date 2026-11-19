package com.example.cdms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cdms.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
