package com.example.cdms.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_bundle_detail", uniqueConstraints = @UniqueConstraint(name = "uk_product_bundle_detail_sale_price_product_bundle_id", columnNames = {
		"sale_price", "product_bundle_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBundleDetail extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_bundle_detail_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_bundle_id", nullable = false)
	private ProductBundle productBundle;

	@Column(name = "qty")
	private Integer qty;

	@Column(name = "sale_price", nullable = false, precision = 19, scale = 2)
	private BigDecimal salePrice;
}