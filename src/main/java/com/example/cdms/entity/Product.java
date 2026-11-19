package com.example.cdms.entity;


import com.example.cdms.enums.AssetType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id") 
    private Long productId;

    @Column(nullable = true)
    private String sku;

    @Column(nullable = true, name = "partner_sku")
    private String partnerSKU;

    @Column(nullable = true, name = "product_name")
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type", length = 30)
    private AssetType assetType;

    @Builder.Default
    @Column(nullable = false, name = "has_serial")
    private Boolean hasSerial = true;

    @Builder.Default
    @Column(nullable = false, name = "has_expiration")
    private Boolean hasExpiration = true;
    
    @Column(nullable = true)
    private String color;
    
    @Column(nullable = true)
    private String size;
    
    @Column(nullable = true)
    private String description;

    @Builder.Default
    @Column(nullable = false, name = "is_active")
    private Boolean isActive = true;

}
