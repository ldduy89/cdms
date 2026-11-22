package com.example.cdms.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.cdms.dto.ProductBySearchResponse;
import com.example.cdms.dto.ProductCategoryResponse;
import com.example.cdms.dto.ProductCreateResponse;
import com.example.cdms.dto.ProductRequest;
import com.example.cdms.dto.ProductResponse;
import com.example.cdms.dto.ProductUnitResponse;
import com.example.cdms.dto.ProductUomRequest;
import com.example.cdms.dto.ProductUomResponse;
import com.example.cdms.entity.Product;
import com.example.cdms.entity.ProductUnit;
import com.example.cdms.entity.ProductUom;
import com.example.cdms.exception.ResourceNotFoundException;
import com.example.cdms.mapper.ProductCategoryMapper;
import com.example.cdms.mapper.ProductMapper;
import com.example.cdms.mapper.ProductUnitMapper;
import com.example.cdms.mapper.ProductUomMapper;
import com.example.cdms.repository.ProductCategoryRepository;
import com.example.cdms.repository.ProductRepository;
import com.example.cdms.repository.ProductUnitRepository;
import com.example.cdms.repository.jdbc.ProductRepositoryJdbc;
import com.example.cdms.service.ProductCategoryService;
import com.example.cdms.service.ProductService;
import com.example.cdms.service.ProductUnitService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductUnitRepository productUnitRepository;
	private final ProductCategoryRepository productCategoryRepository;

	private final ProductMapper productMapper;
	private final ProductUnitMapper productUnitMapper;
	private final ProductCategoryMapper productCategoryMapper;
	private final ProductUomMapper productUomMapper;

	private final ProductRepositoryJdbc productRepositoryJdbc;

	private final ProductUnitService productUnitService;
	private final ProductCategoryService productCategoryService;

	@PersistenceContext
	private EntityManager entityManager;

	private final ObjectMapper objectMapper;

	@Override
	public List<ProductBySearchResponse> search(String keyword, List<String> partnerSkus, List<String> skus,
			int pageIndex, int pageSize) {
		;
		return productRepositoryJdbc.search(keyword, partnerSkus, skus, pageIndex, pageSize);
	}

	@Override
	public ProductResponse findByPartnerSKU(String partnerSKU) {
		return productRepository.findByPartnerSKU(partnerSKU).map(productMapper::toResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with PartnerSku: " + partnerSKU));
	}

	@Override
	public List<ProductCreateResponse> create(List<ProductRequest> requests) {
		productUnitService.ensureUnits(requests);
		productCategoryService.ensureCategories(requests);
		Map<String, Long> ids = productRepositoryJdbc.insertProducts(requests);
		productRepositoryJdbc.insertChildren(requests, ids);
		List<ProductCreateResponse> result = new ArrayList<>();
		Set<String> added = new HashSet<>();
		for (ProductRequest r : requests) {
			Long productId = ids.get(r.sku());
			if (productId == null || !added.add(r.sku())) {
				continue;
			}
			String partnerSku = (r.partnerSKU() == null || r.partnerSKU().isBlank()) ? r.sku() : r.partnerSKU();
			result.add(new ProductCreateResponse(productId, r.sku(), partnerSku));
		}
		return result;
	}

	@Override
	public ProductResponse update(Long id, ProductRequest request) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with ProductId: " + id));

		productUnitService.ensureUnits(List.of(request));
		productCategoryService.ensureCategories(List.of(request));

		productMapper.updateEntity(request, product);
		product.setUnit(productUnitService.getByCode(request.unitCode()));

		product.getProductBundles().clear();
		product.getProductUOMs().clear();
		product.getProductCategories().clear();
		product.getBarcodes().clear();
		productRepository.saveAndFlush(product);

		productRepositoryJdbc.insertChildren(List.of(request), Map.of(product.getSku(), product.getProductId()));
		entityManager.flush();
		entityManager.clear();
		Product newProduct = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with ProductId: " + id));
		return productMapper.toResponse(newProduct);
	}

	@Override
	public List<ProductUnitResponse> findAllProductUnits() {
		return productUnitRepository.findAll().stream().map(productUnitMapper::toResponse).toList();
	}

	@Override
	public List<ProductCategoryResponse> findAllProductCategories() {
		return productCategoryRepository.findAll().stream().map(productCategoryMapper::toResponse).toList();
	}

	@Override
	public List<ProductCreateResponse> importFile(MultipartFile file) throws IOException {
		// TODO Auto-generated method stub
		DataFormatter fmt = new DataFormatter();
		List<ProductRequest> products = new ArrayList<>();
		try (Workbook wb = WorkbookFactory.create(file.getInputStream())) {
			Map<String, List<Map<String, Object>>> unitsBySku = readSheet(wb, "Units", fmt).stream()
					.collect(Collectors.groupingBy(m -> m.get("sku").toString()));
			Map<String, List<Map<String, Object>>> categoriesBySku = readSheet(wb, "Categories", fmt).stream()
					.collect(Collectors.groupingBy(m -> m.get("sku").toString()));
			Map<String, List<Map<String, Object>>> barcodesBySku = readSheet(wb, "Barcodes", fmt).stream()
					.collect(Collectors.groupingBy(m -> m.get("sku").toString()));
			Map<String, List<Map<String, Object>>> bundlesBySku = readSheet(wb, "Bundles", fmt).stream()
					.collect(Collectors.groupingBy(m -> m.get("sku").toString()));
			Map<String, List<Map<String, Object>>> bundleDetailsByKey = readSheet(wb, "BundleDetails", fmt).stream()
					.collect(Collectors.groupingBy(m -> m.get("sku").toString() + "|" + m.get("childSku").toString()));

			for (Map<String, Object> o : readSheet(wb, "Products", fmt)) {
				o.put("units", unitsBySku.get(o.get("sku").toString()));
				o.put("categories", categoriesBySku.get(o.get("sku").toString()));
				o.put("barcodes", barcodesBySku.get(o.get("sku").toString()));
				List<Map<String, Object>> bundles = bundlesBySku.get(o.get("sku").toString());
				if (bundles != null) {
					for (Map<String, Object> b : bundles) {
						b.put("bundleDetails",
								bundleDetailsByKey.get(b.get("sku").toString() + "|" + b.get("childSku").toString()));
					}
				}
				o.put("productBundles", bundles);
				products.add(objectMapper.convertValue(o, ProductRequest.class));
			}
		}

		return this.create(products);
	}

	private List<Map<String, Object>> readSheet(Workbook wb, String name, DataFormatter fmt) {
		Sheet s = wb.getSheet(name);
		if (s == null) {
			return List.of();
		}
		Map<String, Integer> cols = new HashMap<>();

		for (Cell c : s.getRow(0)) {
			String key = fmt.formatCellValue(c).trim();
			if (!key.isEmpty()) {
				cols.put(key, c.getColumnIndex());
			}
		}

		List<Map<String, Object>> out = new ArrayList<>();

		for (int r = 1; r <= s.getLastRowNum(); r++) {
			Row row = s.getRow(r);
			if (row == null || isBlankRow(row, fmt)) {
				continue;
			}
			Map<String, Object> m = new LinkedHashMap<>();
			for (String col : cols.keySet()) {
				Integer idx = cols.get(col);
				if (idx == null) {
					continue;
				}
				Cell cell = row.getCell(idx, MissingCellPolicy.RETURN_NULL_AND_BLANK);
				if (cell == null) {
					continue;
				}
				String value = fmt.formatCellValue(cell).trim();

				if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
					value = value.toLowerCase();
				}

				m.put(col, value.isEmpty() ? null : value);
			}
			out.add(m);
		}
		return out;
	}

	private boolean isBlankRow(Row row, DataFormatter fmt) {
		for (Cell c : row) {
			if (!fmt.formatCellValue(c).trim().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<ProductUomResponse> createUom(List<ProductUomRequest> requests) {
		Set<String> skus = requests.stream().map(ProductUomRequest::sku).collect(Collectors.toSet());
		Map<String, Product> products = productRepository.findAllBySkuIn(skus).stream()
				.collect(Collectors.toMap(Product::getSku, p -> p));

		List<String> missingSkus = skus.stream().filter(s -> !products.containsKey(s)).toList();

		if (!missingSkus.isEmpty()) {
			throw new ResourceNotFoundException("sku not found: " + String.join(", ", missingSkus));
		}

		Set<String> unitCodes = requests.stream().map(ProductUomRequest::unitCode).collect(Collectors.toSet());
		Map<String, ProductUnit> units = productUnitRepository.findByCodeIn(unitCodes).stream()
				.collect(Collectors.toMap(ProductUnit::getCode, u -> u));

		List<String> missingUnits = unitCodes.stream().filter(c -> !units.containsKey(c)).toList();

		if (!missingUnits.isEmpty()) {
			throw new ResourceNotFoundException("unitCode not found: " + String.join(", ", missingUnits));
		}
		List<ProductUom> uoms = new ArrayList<>();
		for (ProductUomRequest r : requests) {
			Product product = products.get(r.sku());
			ProductUom uom = ProductUom.builder()
					.unit(units.get(r.unitCode()))
					.baseUnitQty(r.baseUnitQty())
					.length(r.length()).width(r.width()).height(r.height()).weight(r.weight())
					.costPrice(r.costPrice()).sellingPrice(r.sellingPrice())
					.isBaseUnit(false)
					.isStockConversionEnabled(false)
					.build();
			uom.setProduct(product);
			uoms.add(uom);
		}
		return productUomMapper.toResponses(productRepositoryJdbc.inserProductUom(uoms));
	}

}
