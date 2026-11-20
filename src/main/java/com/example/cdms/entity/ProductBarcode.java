package com.example.cdms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_barcode")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ProductBarcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_barcode_id")
    private Long productBarcodeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sku", referencedColumnName = "sku", nullable = false)
    private Product product;

    @Column(nullable = false, unique = true)
    private String barcode;

    @Column(name = "partner_sku")
    private String partnerSKU;

    private String name;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "ref_code")
    private String refCode;
}