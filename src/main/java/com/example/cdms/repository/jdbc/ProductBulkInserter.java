package com.example.cdms.repository.jdbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductRequest.BarcodeItemRequest;
import com.example.cdms.dto.ProductRequest.ProductBundleDetailRequest;
import com.example.cdms.dto.ProductRequest.ProductBundleItemRequest;
import com.example.cdms.dto.ProductRequest.ProductUnitItemRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class ProductBulkInserter {

	private static final int CHUNK_SIZE = 800;

	private static final List<String> COLUMNS = List.of("sku", "partner_sku", "product_name", "description", "color",
			"size", "packing_note", "avatar_url", "hs_code", "country_of_origin", "product_tax_code",
			"product_tax_name", "tax_rate", "ref_code", "unit_code", "asset_type", "serial_type", "outbound_type",
			"warehouse_category", "is_physical", "is_bar_code", "is_discreet", "is_ignore_received_date",
			"is_ignore_ir_id", "is_active", "has_serial", "has_expiration", "is_lot_number", "min_inbound_shelf_life",
			"min_outbound_shelf_life", "num_days_warning_exp_date", "num_days_export_before_exp_date", "product_life",
			"outbound_limit_condition_code", "expired_condition_code");

	private final JdbcTemplate jdbcTemplate;

	public Map<String, Long> insertProducts(List<ProductRequest> requests) {
		Map<String, Long> ids = new HashMap<>();
		for (int i = 0; i < requests.size(); i += CHUNK_SIZE) {
			List<ProductRequest> chunk = requests.subList(i, Math.min(i + CHUNK_SIZE, requests.size()));
			ids.putAll(insertChunk(chunk));
		}
		return ids;
	}

	private Map<String, Long> insertChunk(List<ProductRequest> chunk) {
		String rowSql = "(" + "?,".repeat(COLUMNS.size() - 1) + "?, now(), now())";

		String sql = "INSERT INTO product (" + String.join(", ", COLUMNS) + ", created_at, updated_at) VALUES "
				+ String.join(", ", Collections.nCopies(chunk.size(), rowSql)) + " ON CONFLICT (sku) DO NOTHING"
				+ " RETURNING sku, product_id";

		List<Object> params = new ArrayList<>();
		for (ProductRequest r : chunk) {
			params.addAll(toParams(r));
		}

		Map<String, Long> ids = new HashMap<>();
		jdbcTemplate.query(sql, rs -> {
			ids.put(rs.getString("sku"), rs.getLong("product_id"));
		}, params.toArray());
		return ids;
	}

	private List<Object> toParams(ProductRequest r) {
		return Arrays.<Object>asList(r.sku(), r.partnerSKU(), r.productName(), r.description(), r.color(), r.size(),
				r.packingNote(), r.avatarURL(), r.hsCode(), r.countryOfOrigin(), r.productTaxCode(), r.productTaxName(),
				r.taxRate(), r.refCode(), r.unitCode(), r.assetType().name(), r.serialType(), r.outboundType().name(),
				r.warehouseCategory(), r.isPhysical(), r.isBarCode(), r.isDiscreet(), r.isIgnoreReceivedDate(),
				r.isIgnoreIRId(), Boolean.TRUE, r.isTrackingSerial(), r.isExpiryDate(), r.isLotNumber(),
				r.minInboundShelfLife(), r.minOutboundShelfLife(), r.numDaysWarningExpDate(),
				r.numDaysExportBeforeExpDate(), r.productLife(), r.outboundLimitConditionCode(),
				r.expiredConditionCode());
	}

	public void insertChildren(List<ProductRequest> requests, Map<String, Long> productIds) {
	    List<Object[]> uomRows = new ArrayList<>();
	    List<Object[]> categoryRows = new ArrayList<>();
	    List<Object[]> barcodeRows = new ArrayList<>();
	    List<Object[]> detailRows = new ArrayList<>();

	    Set<String> childSkus = requests.stream()
	            .filter(r -> r.productBundles() != null)
	            .flatMap(r -> r.productBundles().stream())
	            .map(ProductBundleItemRequest::sku)
	            .filter(Objects::nonNull)
	            .collect(Collectors.toSet());
	    Map<String, Long> childIds = resolveProductIds(childSkus);

	    Set<String> done = new HashSet<>();
	    for (ProductRequest r : requests) {
	        Long productId = productIds.get(r.sku());
	        if (productId == null || !done.add(r.sku())) {
	            continue;
	        }

	        for (ProductUnitItemRequest u : r.productUnits()) {
	            uomRows.add(new Object[] { productId, u.unitCode(), u.baseUnitQty(), u.weight(), u.length(),
	                    u.width(), u.height(), u.costPrice(), u.sellingPrice(), u.isBaseUnit(),
	                    u.isStockConversionEnabled() });
	        }

	        Set<String> seenCategories = new HashSet<>();
	        for (var c : r.categories()) {
	            if (seenCategories.add(c.code())) {
	                categoryRows.add(new Object[] { productId, c.code() });
	            }
	        }

	        if (r.barcodes() != null) {
	            for (BarcodeItemRequest b : r.barcodes()) {
	                barcodeRows.add(new Object[] { productId, b.barcode(), b.partnerSKU(), b.name(),
	                        b.avatarURL(), b.refCode() });
	            }
	        }

	        if (r.productBundles() != null) {
	            for (ProductBundleItemRequest b : r.productBundles()) {
	                Long childId = childIds.get(b.sku());
	                if (childId == null) {
	                    throw new IllegalArgumentException("Not found bundle: " + b.sku());
	                }
	                Long bundleId = insertBundle(productId, childId, b);
	                if (b.bundleDetails() != null) {
	                    for (ProductBundleDetailRequest d : b.bundleDetails()) {
	                        detailRows.add(new Object[] { bundleId, d.qty(), d.salePrice() });
	                    }
	                }
	            }
	        }
	    }

	    batch("""
	            INSERT INTO product_uom (product_id, unit_code, base_unit_qty, weight, length, width, height,
	                cost_price, selling_price, is_base_unit, is_stock_conversion_enabled, created_at, updated_at)
	            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now())
	            """, uomRows);

	    batch("""
	            INSERT INTO product_category_mapping (product_id, category_code)
	            VALUES (?, ?)
	            """, categoryRows);

	    batch("""
	            INSERT INTO product_barcode (product_id, barcode, partner_sku, name, avatar_url, ref_code)
	            VALUES (?, ?, ?, ?, ?, ?)
	            """, barcodeRows);

	    batch("""
	            INSERT INTO product_bundle_detail (product_bundle_id, qty, sale_price, created_at, updated_at)
	            VALUES (?, ?, ?, now(), now())
	            """, detailRows);
	}

	private Long insertBundle(Long parentId, Long childId, ProductBundleItemRequest b) {
	    return jdbcTemplate.queryForObject("""
	            INSERT INTO product_bundle (parent_product_id, child_product_id, unit_code, qty,
	                available_percentage, created_at, updated_at)
	            VALUES (?, ?, ?, ?, ?, now(), now())
	            RETURNING product_bundle_id
	            """, Long.class, parentId, childId, b.unitCode(), b.qty(), b.availablePercentage());
	}

	private void batch(String sql, List<Object[]> rows) {
		if (!rows.isEmpty()) {
			jdbcTemplate.batchUpdate(sql, rows);
		}
	}
	
	public Map<String, Long> resolveProductIds(Collection<String> skus) {
	    Map<String, Long> ids = new HashMap<>();
	    if (skus.isEmpty()) {
	        return ids;
	    }
	    String placeholders = String.join(", ", Collections.nCopies(skus.size(), "?"));
	    jdbcTemplate.query(
	            "SELECT sku, product_id FROM product WHERE sku IN (" + placeholders + ")",
	            rs -> { ids.put(rs.getString("sku"), rs.getLong("product_id")); },
	            skus.toArray());
	    return ids;
	}
}