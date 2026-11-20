package com.example.cdms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_bundle_detail")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
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

    @Column(name = "sale_price", nullable = false, unique = true, precision = 19, scale = 2)
    private BigDecimal salePrice;
}