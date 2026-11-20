package com.example.cdms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_category_mapping",
       uniqueConstraints = @UniqueConstraint(
               name = "uk_product_category_mapping_sku_code",
               columnNames = {"sku", "category_code"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ProductCategoryMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_category_mapping_id")
    private Long productCategoryMappingId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sku", referencedColumnName = "sku", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_code", referencedColumnName = "code", nullable = false)
    private ProductCategory category;
}