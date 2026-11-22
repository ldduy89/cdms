package com.example.cdms.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import net.datafaker.Faker;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class ProductApiSeeder {

	private static final String[][] CATEGORIES = {
			{ "CATEGORY001", "Phone" },
			{ "CATEGORY002", "Accessory" },
			{ "CATEGORY003", "Computer" },
			{ "CATEGORY004", "Household goods" } };
	private static final String[] ASSET = { "Single", "Single", "Single", "MultipleBarcodes" };
	private static final String[] OUTBOUND = { "NotDefined", "FIFO", "LIFO", "FEFO", "LEFO" };
	private static final String[] SIZES = { "S", "M", "L", "XL", "XXL" };

	private final Faker faker = new Faker(Locale.forLanguageTag("vi"));
	private final Random random;
	private final String prefix;
	private final Set<String> barcodes = new HashSet<>();
	private final Set<String> salePrices = new HashSet<>();
	private final List<String> simpleSkus = new ArrayList<>();

	public ProductApiSeeder(String prefix, long seed) {
		this.prefix = prefix;
		this.random = new Random(seed);
	}

	public static void main(String[] args) throws Exception {
		int count = args.length > 0 ? Integer.parseInt(args[0]) : 500000;
		String prefix = args.length > 1 ? args[1] : "FK";
		String baseUrl = args.length > 2 ? args[2] : "http://localhost:8080";
		int batch = args.length > 3 ? Integer.parseInt(args[3]) : 10000;
		long seed = args.length > 4 ? Long.parseLong(args[4]) : System.currentTimeMillis();
		new ProductApiSeeder(prefix, seed).run(count, baseUrl, batch);
	}

	public void run(int count, String baseUrl, int batch) throws Exception {
		List<Map<String, Object>> all = build(count);
		RestClient client = RestClient.builder().baseUrl(baseUrl).build();
		ObjectMapper om = new ObjectMapper();

		int created = 0;
		int failed = 0;
		for (int i = 0; i < all.size(); i += batch) {
			List<Map<String, Object>> chunk = all.subList(i, Math.min(i + batch, all.size()));
			try {
				String body = client.post()
						.uri("/api/products")
						.contentType(MediaType.APPLICATION_JSON)
						.body(chunk)
						.retrieve()
						.body(String.class);
				JsonNode res = om.readTree(body);
				created += res.size();
				System.out.printf("%d-%d: created %d%n", i + 1, i + chunk.size(), res.size());
			} catch (RestClientResponseException e) {
				failed += chunk.size();
				System.out.printf("%d-%d error %d: %s%n", i + 1, i + chunk.size(),
						e.getStatusCode().value(), e.getResponseBodyAsString());
			}
		}
		System.out.printf("Finish: push %d, create %d, error %d%n", all.size(), created, failed);
	}

	private List<Map<String, Object>> build(int count) {
		List<Map<String, Object>> out = new ArrayList<>();
		for (int i = 1; i <= count; i++) {
			String sku = String.format("%s%05d", prefix, i);
			boolean bundle = i > 10 && i % 10 == 0 && simpleSkus.size() >= 2;
			String asset = bundle ? "Bundle" : pick(ASSET);

			Map<String, Object> p = new LinkedHashMap<>();
			p.put("sku", sku);
			p.put("partnerSKU", "PARTNER" + sku);
			p.put("productName", faker.commerce().productName());
			p.put("unitCode", "CAI");
			p.put("unitName", "Cái");
			p.put("assetType", asset);
			p.put("serialType", 1 + random.nextInt(2));
			p.put("outboundType", pick(OUTBOUND));
			p.put("warehouseCategory", 1 + random.nextInt(2));
			p.put("isTrackingSerial", random.nextBoolean());
			p.put("isExpiryDate", random.nextBoolean());
			p.put("isLotNumber", random.nextBoolean());
			p.put("isBarCode", true);
			p.put("isPhysical", true);
			p.put("color", faker.color().name());
			p.put("size", pick(SIZES));
			p.put("description", faker.lorem().sentence(8));
			p.put("hsCode", String.valueOf(10_000_000 + random.nextInt(89_999_999)));
			p.put("countryOfOrigin", "VN");
			p.put("taxRate", pick(new double[] { 0, 5, 8, 10 }));
			p.put("refCode", "REF-" + sku);

			int cost = 50_000 + random.nextInt(20_000_000);
			List<Map<String, Object>> units = new ArrayList<>();
			units.add(unit("CAI", "Cái", 1, cost, (int) (cost * 1.25), true, false));
			if (random.nextDouble() < 0.3) {
				units.add(unit("THUNG", "Thùng", 12, cost * 12, (int) (cost * 12 * 1.2), false, true));
			}
			p.put("productUnits", units);

			List<String[]> picked = new ArrayList<>(List.of(CATEGORIES));
			Collections.shuffle(picked, random);
			List<Map<String, Object>> cats = new ArrayList<>();
			for (int c = 0; c < 1 + random.nextInt(2); c++) {
				cats.add(Map.of("code", picked.get(c)[0], "name", picked.get(c)[1]));
			}
			p.put("categories", cats);

			List<Map<String, Object>> bcs = new ArrayList<>();
			if (asset.equals("MultipleBarcodes")) {
				for (int b = 0; b < 2 + random.nextInt(2); b++) {
					bcs.add(Map.of(
							"barcode", newBarcode(),
							"partnerSKU", "PARTNERBC-" + randomCode(6),
							"name", faker.commerce().material(),
							"avatarURL", faker.internet().image(),
							"refCode", "REF-BC-" + randomCode(4)));
				}
			}
			p.put("barcodes", bcs);

			List<Map<String, Object>> bundles = new ArrayList<>();
			if (asset.equals("Bundle")) {
				List<String> children = new ArrayList<>(simpleSkus);
				Collections.shuffle(children, random);
				for (String child : children.subList(0, 2)) {
					List<Map<String, Object>> details = new ArrayList<>();
					for (int d = 0; d < 1 + random.nextInt(2); d++) {
						details.add(Map.of("qty", 1 + random.nextInt(3), "salePrice", newSalePrice()));
					}
					bundles.add(Map.of(
							"sku", child,
							"unitCode", "CAI",
							"qty", 1 + random.nextInt(3),
							"availablePercentage", pick(new double[] { 50, 80, 100 }),
							"bundleDetails", details));
				}
			} else if (asset.equals("Single")) {
				simpleSkus.add(sku);
			}
			p.put("productBundles", bundles);

			out.add(p);
		}
		return out;
	}

	private Map<String, Object> unit(String code, String name, int baseQty, int cost, int sell,
			boolean base, boolean stock) {
		Map<String, Object> u = new LinkedHashMap<>();
		u.put("unitCode", code);
		u.put("unitName", name);
		u.put("baseUnitQty", baseQty);
		u.put("costPrice", cost);
		u.put("sellingPrice", sell);
		u.put("weight", round1(20 + random.nextDouble() * 1980));
		u.put("length", round1(5 + random.nextDouble() * 35));
		u.put("width", round1(3 + random.nextDouble() * 27));
		u.put("height", round1(0.5 + random.nextDouble() * 19.5));
		u.put("isBaseUnit", base);
		u.put("isStockConversionEnabled", stock);
		return u;
	}

	private String newBarcode() {
		String code;
		do {
			code = faker.numerify("##############");
		} while (!barcodes.add(code));
		return code;
	}

	private BigDecimal newSalePrice() {
		BigDecimal p;
		do {
			p = BigDecimal.valueOf(100_000 + random.nextInt(50_000_000) + random.nextInt(4) * 0.25)
					.setScale(2, RoundingMode.HALF_UP);
		} while (!salePrices.add(p.toPlainString()));
		return p;
	}

	private String randomCode(int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append((char) ('A' + random.nextInt(26)));
		}
		return sb.toString();
	}

	private static double round1(double v) {
		return Math.round(v * 10) / 10.0;
	}

	private <T> T pick(T[] values) {
		return values[random.nextInt(values.length)];
	}

	private double pick(double[] values) {
		return values[random.nextInt(values.length)];
	}
}