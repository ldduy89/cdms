package com.example.cdms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_bundle",
uniqueConstraints = @UniqueConstraint(
        name = "uk_product_bundle_parent_sku",
        columnNames = {"parent_sku", "sku"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ProductBundle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_bundle_id")
    private Long productBundleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_sku", referencedColumnName = "sku")
    private Product product;


    @Column(nullable = false, length = 100)
    private String sku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_code", referencedColumnName = "code")
    private ProductUnit unit;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "available_percentage")
    private Double availablePercentage;

    @Builder.Default
    @OneToMany(mappedBy = "productBundle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductBundleDetail> bundleDetails = new ArrayList<>();

    public void addDetail(ProductBundleDetail detail) {
        bundleDetails.add(detail);
        detail.setProductBundle(this);
    }
}