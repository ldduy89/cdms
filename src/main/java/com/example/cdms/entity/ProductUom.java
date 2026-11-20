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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_uom")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ProductUom extends BaseEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_uom_id") 
    private Long productUomId;
	
	@Column(name = "base_unit_qty", nullable = true)
    private Integer baseUnitQty;

    @Column(nullable = true)
    private Double length;

    @Column(nullable = true)
    private Double width;

    @Column(nullable = true)
    private Double height;

    @Column(nullable = true)
    private Double weight;

    @Column(name = "cost_price", nullable = true)
    private BigDecimal costPrice;

    @Column(name = "selling_price", nullable = true)
    private BigDecimal sellingPrice;

    @Builder.Default
    @Column(name = "is_base_unit")
    private Boolean isBaseUnit = true;

    @Builder.Default
    @Column(name = "is_stock_conversion_enabled")
    private Boolean isStockConversionEnabled = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sku", referencedColumnName = "sku", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unit_code", referencedColumnName = "code", nullable = false)
    private ProductUnit unit;
    
}
