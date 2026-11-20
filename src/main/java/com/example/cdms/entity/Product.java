package com.example.cdms.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.cdms.enums.AssetType;
import com.example.cdms.enums.OutboundType;
import com.example.cdms.enums.SerialType;
import com.example.cdms.enums.WarehouseCategoryType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    // ===== Thông tin cơ bản =====

    @Column(nullable = false, unique = true, length = 100)
    private String sku;

    @Column(name = "partner_sku")
    private String partnerSKU;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(length = 255)
    private String description;

    @Column(length = 50)
    private String color;

    @Column(length = 50)
    private String size;

    @Column(name = "packing_note")
    private String packingNote;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "hs_code")
    private String hsCode;

    @Column(name = "country_of_origin")
    private String countryOfOrigin;

    @Column(name = "product_tax_code")
    private String productTaxCode;

    @Column(name = "product_tax_name")
    private String productTaxName;

    @Column(name = "tax_rate")
    private Double taxRate;

    @Column(name = "ref_code")
    private String refCode;


    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type", length = 30)
    private AssetType assetType;

    @Column(name = "serial_type")
    private SerialType serialType;

    @Enumerated(EnumType.STRING)
    @Column(name = "outbound_type", length = 20)
    private OutboundType outboundType;

    @Column(name = "warehouse_category")
    private WarehouseCategoryType warehouseCategory;

    @Builder.Default
    @Column(name = "is_physical", nullable = false)
    private Boolean isPhysical = true;

    @Builder.Default
    @Column(name = "is_bar_code", nullable = false)
    private Boolean isBarCode = false;

    @Builder.Default
    @Column(name = "is_discreet", nullable = false)
    private Boolean isDiscreet = false;

    @Builder.Default
    @Column(name = "is_ignore_received_date", nullable = false)
    private Boolean isIgnoreReceivedDate = true;

    @Builder.Default
    @Column(name = "is_ignore_ir_id", nullable = false)
    private Boolean isIgnoreIRId = true;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Builder.Default
    @Column(name = "has_serial", nullable = false)
    private Boolean hasSerial = false;

    @Builder.Default
    @Column(name = "has_expiration", nullable = false)
    private Boolean hasExpiration = false;

    @Builder.Default
    @Column(name = "is_lot_number", nullable = false)
    private Boolean isLotNumber = false;

    @Column(name = "min_inbound_shelf_life")
    private Integer minInboundShelfLife;

    @Column(name = "min_outbound_shelf_life")
    private Integer minOutboundShelfLife;

    @Column(name = "num_days_warning_exp_date")
    private Integer numDaysWarningExpDate;

    @Column(name = "num_days_export_before_exp_date")
    private Integer numDaysExportBeforeExpDate;

    @Column(name = "product_life")
    private Integer productLife;

    @Column(name = "outbound_limit_condition_code")
    private String outboundLimitConditionCode;

    @Column(name = "expired_condition_code")
    private String expiredConditionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_code", referencedColumnName = "code")
    private ProductUnit unit;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductUom> productUOMs = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCategoryMapping> productCategories = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductBundle> productBundles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductBarcode> barcodes = new ArrayList<>();

    public void addCategory(ProductCategory category) {
        ProductCategoryMapping mapping = ProductCategoryMapping.builder()
                .product(this)
                .category(category)
                .build();
        productCategories.add(mapping);
    }

    public void addBundle(ProductBundle bundle) {
        productBundles.add(bundle);
        bundle.setProduct(this);
    }

    public void addBarcode(ProductBarcode barcode) {
        barcodes.add(barcode);
        barcode.setProduct(this);
    }

    public void addUOM(ProductUom uom) {
        productUOMs.add(uom);
        uom.setProduct(this);
    }
}