package com.example.cdms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_category_mapping", uniqueConstraints = @UniqueConstraint(name = "uk_product_category_mapping_product_code", columnNames = {
		"product_id", "category_code" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategoryMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_category_mapping_id")
	private Long productCategoryMappingId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_code", referencedColumnName = "code", nullable = false)
	private ProductCategory category;
}