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
import org.springframework.util.StringUtils;

import com.example.cdms.dto.ProductBySearchResponse;
import com.example.cdms.dto.ProductCategoryResponse;
import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductRequest.BarcodeItemRequest;
import com.example.cdms.dto.ProductRequest.ProductBundleDetailRequest;
import com.example.cdms.dto.ProductRequest.ProductBundleItemRequest;
import com.example.cdms.dto.ProductRequest.ProductUnitItemRequest;
import com.example.cdms.entity.ProductUom;
import com.example.cdms.entity.ProductUom;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class ProductRepositoryJdbc {

	private final JdbcTemplate jdbcTemplate;

	private static final int CHUNK_SIZE = 1000;

	private static final List<String> COLUMNS = List.of("sku", "partner_sku", "product_name", "description", "color",
			"size", "packing_note", "avatar_url", "hs_code", "country_of_origin", "product_tax_code",
			"product_tax_name", "tax_rate", "ref_code", "unit_code", "asset_type", "serial_type", "outbound_type",
			"warehouse_category", "is_physical", "is_bar_code", "is_discreet", "is_ignore_received_date",
			"is_ignore_ir_id", "is_active", "is_tracking_serial", "is_expiry_date", "is_lot_number",
			"min_inbound_shelf_life", "min_outbound_shelf_life", "num_days_warning_exp_date",
			"num_days_export_before_exp_date", "product_life", "outbound_limit_condition_code",
			"expired_condition_code");

	public List<ProductBySearchResponse> search(String keyword, List<String> partnerSkus, List<String> skus,
			int pageIndex, int pageSize) {
		StringBuilder sql = new StringBuilder("""
				SELECT p.product_id, p.sku, p.partner_sku, p.product_name, p.asset_type,
				       p.is_tracking_serial, p.is_expiry_date, p.color, p.size, p.description, p.is_active
				FROM product p
				WHERE 1 = 1
				""");
		List<Object> params = new ArrayList<>();

		if (StringUtils.hasText(keyword)) {
			sql.append(" AND (LOWER(p.sku) LIKE ? OR LOWER(p.product_name) LIKE ?)");
			String like = "%" + keyword.trim().toLowerCase() + "%";
			params.add(like);
			params.add(like);
		}

		if (partnerSkus != null && !partnerSkus.isEmpty()) {
			sql.append(" AND p.partner_sku IN (").append(placeholders(partnerSkus.size())).append(")");
			params.addAll(partnerSkus);
		}

		if (skus != null && !skus.isEmpty()) {
			sql.append(" AND p.sku IN (").append(placeholders(skus.size())).append(")");
			params.addAll(skus);
		}

		sql.append(" ORDER BY p.product_id LIMIT ? OFFSET ?");
		params.add(pageSize);
		params.add((long) pageIndex * pageSize);

		List<ProductBySearchResponse> base = jdbcTemplate.query(sql.toString(),
				(rs, n) -> new ProductBySearchResponse(rs.getLong("product_id"), rs.getString("product_name"),
						rs.getString("sku"), rs.getString("partner_sku"), rs.getString("asset_type"),
						rs.getBoolean("is_tracking_serial"), rs.getBoolean("is_expiry_date"), rs.getString("color"),
						rs.getString("size"), rs.getString("description"), rs.getBoolean("is_active"), List.of(),
						List.of()),
				params.toArray());

		if (base.isEmpty()) {
			return base;
		}

		List<Long> ids = base.stream().map(ProductBySearchResponse::productId).toList();
		String idPh = placeholders(ids.size());

		Map<Long, List<String>> units = new HashMap<>();
		jdbcTemplate.query("SELECT product_id, unit_code FROM product_uom WHERE product_id IN (" + idPh + ")", rs -> {
			units.computeIfAbsent(rs.getLong("product_id"), k -> new ArrayList<>()).add(rs.getString("unit_code"));
		}, ids.toArray());

		Map<Long, List<ProductCategoryResponse>> categories = new HashMap<>();
		jdbcTemplate.query("""
				SELECT m.product_id, c.code, c.name
				FROM product_category_mapping m
				JOIN product_category c ON c.code = m.category_code
				WHERE m.product_id IN (""" + idPh + ")", rs -> {
			categories.computeIfAbsent(rs.getLong("product_id"), k -> new ArrayList<>())
					.add(new ProductCategoryResponse(rs.getString("code"), rs.getString("name")));
		}, ids.toArray());

		return base.stream()
				.map(p -> new ProductBySearchResponse(p.productId(), p.sku(), p.partnerSKU(), p.productName(),
						p.assetType(), p.isTrackingSerial(), p.isExpiryDate(), p.color(), p.size(), p.description(),
						p.isActive(), units.getOrDefault(p.productId(), List.of()),
						categories.getOrDefault(p.productId(), List.of())))
				.toList();
	}

	private static String placeholders(int n) {
		return String.join(", ", Collections.nCopies(n, "?"));
	}

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

		Set<String> childSkus = requests.stream().filter(r -> r.productBundles() != null)
				.flatMap(r -> r.productBundles().stream()).map(ProductBundleItemRequest::sku).filter(Objects::nonNull)
				.collect(Collectors.toSet());
		Map<String, Long> childIds = resolveProductIds(childSkus);

		Set<String> done = new HashSet<>();
		for (ProductRequest r : requests) {
			Long productId = productIds.get(r.sku());
			if (productId == null || !done.add(r.sku())) {
				continue;
			}

			for (ProductUnitItemRequest u : r.productUnits()) {
				uomRows.add(new Object[] { productId, u.unitCode(), u.baseUnitQty(), u.weight(), u.length(), u.width(),
						u.height(), u.costPrice(), u.sellingPrice(), u.isBaseUnit(), u.isStockConversionEnabled() });
			}

			Set<String> seenCategories = new HashSet<>();
			for (var c : r.categories()) {
				if (seenCategories.add(c.categoryCode())) {
					categoryRows.add(new Object[] { productId, c.categoryCode() });
				}
			}

			if (r.barcodes() != null) {
				for (BarcodeItemRequest b : r.barcodes()) {
					barcodeRows.add(new Object[] { productId, b.barcode(), b.partnerSKU(), b.name(), b.avatarURL(),
							b.refCode() });
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
		jdbcTemplate.query("SELECT sku, product_id FROM product WHERE sku IN (" + placeholders + ")", rs -> {
			ids.put(rs.getString("sku"), rs.getLong("product_id"));
		}, skus.toArray());
		return ids;
	}

	public List<ProductUom> inserProductUom(List<ProductUom> uoms) {
		List<Object> params = new ArrayList<>();
		for (ProductUom u : uoms) {
			params.addAll(Arrays.<Object>asList(u.getProduct().getProductId(), u.getUnit().getCode(),
					u.getBaseUnitQty(), u.getWeight(), u.getLength(), u.getWidth(), u.getHeight(), u.getCostPrice(),
					u.getSellingPrice(), u.getIsBaseUnit(), u.getIsStockConversionEnabled()));
		}

		Map<String, Long> ids = new HashMap<>();
		
		final int cols = 11;
		String row = "(" + "?, ".repeat(10) + "?, now(), now())";
		String sql = """
				INSERT INTO product_uom (product_id, unit_code, base_unit_qty, weight, length, width, height,
					cost_price, selling_price, is_base_unit, is_stock_conversion_enabled, created_at, updated_at)
				VALUES """
				+ String.join(", ", Collections.nCopies(uoms.size(), row))
				+ " RETURNING product_uom_id, product_id, unit_code";

		
		jdbcTemplate.query(sql, rs -> {
			ids.put(rs.getLong("product_id") + "|" + rs.getString("unit_code"), rs.getLong("product_uom_id"));
		}, params.toArray());

		for (ProductUom u : uoms) {
			String key = u.getProduct().getProductId() + "|" + u.getUnit().getCode();
			u.setProductUomId(ids.get(key));
		}
		return uoms;
	}
}