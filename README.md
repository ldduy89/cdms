# CDMS - Product Catalog Management Service

CDMS is a Spring Boot service that manages a product catalog: products, units of measure (UOM), units, categories, barcodes and product bundles. It supports single API creation, bulk creation, Excel import, a scheduled sync job from an external source, and containerized deployment with PostgreSQL.

## Tech Stack

| Component | Version / Notes |
|---|---|
| Java | 25 |
| Spring Boot | 4.1.2 (Spring Framework 7, Jackson 3, Tomcat 11) |
| Hibernate ORM | 7.4 |
| PostgreSQL | 17 |
| MapStruct | 1.6.x (entity/DTO mapping) |
| Apache POI | 5.4.1 (Excel import) |
| Datafaker | 2.4.2 (test data generation, test scope) |
| Maven | Build tool, Maven Wrapper included |
| Docker / Docker Compose | Containerized deployment |

## Features

- Create products in batches (`POST /api/products`) with validation and per-item results.
- Bulk insert using JDBC multi-row `INSERT ... ON CONFLICT (sku) DO NOTHING ... RETURNING`, so existing SKUs are skipped without failing the batch.
- Update a product by ID (`PUT /api/products/{id}`), replacing child collections (units, categories, bundles, barcodes).
- Search with keyword, SKU list, partner SKU list and pagination (`GET /api/products`).
- Look up a product by partner SKU.
- Add units of measure to existing products (`POST /api/products/units`).
- List units and categories.
- Import products from a multi-sheet Excel file (`POST /api/products/import`).
- Scheduled daily synchronization from an external products API (disabled by default).
- Friendly error responses (RFC 7807 `ProblemDetail`) for not-found, validation and duplicate-key conflicts.

## Project Structure

```
cdms/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── mvnw, mvnw.cmd, .mvn/
└── src/
    ├── main/
    │   ├── java/com/example/cdms/
    │   │   ├── CdmsApplication.java
    │   │   ├── controller/      REST endpoints
    │   │   ├── dto/             request / response records
    │   │   ├── entity/          JPA entities
    │   │   ├── enums/           AssetType, OutboundType, enum converters
    │   │   ├── exception/       ResourceNotFoundException, GlobalExceptionHandler
    │   │   ├── mapper/          MapStruct mappers
    │   │   ├── repository/      Spring Data JPA repositories
    │   │   │   └── jdbc/        JDBC bulk inserters
    │   │   ├── service/         service interfaces
    │   │   │   └── impl/        service implementations
    │   │   └── sync/            scheduled sync job
    │   └── resources/
    │       ├── application.yaml
    │       └── META-INF/additional-spring-configuration-metadata.json
    └── test/
        └── java/com/example/cdms/
            ├── CdmsApplicationTests.java
            └── tools/ProductApiSeeder.java   fake data generator (calls the API)
```

## Data Model

Main tables (created automatically by Hibernate with `ddl-auto: update`):

| Table | Purpose | Key relations |
|---|---|---|
| `product` | Product master data | `sku` unique, `unit_code` -> `product_unit.code` |
| `product_unit` | Unit master (CAI, THUNG, ...) | `code` unique |
| `product_uom` | Unit conversion for a product | `product_id` -> `product`, `unit_code` -> `product_unit` |
| `product_category` | Category master | `code` unique |
| `product_category_mapping` | Product <-> category | `product_id`, `category_code`, unique pair |
| `product_barcode` | Barcodes of a product | `product_id`, `barcode` unique |
| `product_bundle` | Bundle parent/child link | `product_id`, `child_product_id` (both -> `product`) |
| `product_bundle_detail` | Sale prices in a bundle | `product_bundle_id`, `sale_price` unique |

## Configuration

`src/main/resources/application.yaml`:

```yaml
server:
  port: 8080

spring:
  application:
    name: cdms
  datasource:
    url: jdbc:postgresql://localhost:5432/cdms
    username: cdms
    password: cdms
  jpa:
    hibernate:
      ddl-auto: update
    open-in-view: false
```

## Running Locally

Prerequisites: JDK 25, Maven (or the included wrapper), PostgreSQL 17.

1. Create the database and user:

   ```sql
   CREATE USER cdms WITH PASSWORD 'cdms';
   CREATE DATABASE cdms OWNER cdms;
   ```

2. Start the application:

   ```bash
   ./mvnw spring-boot:run
   ```

   The API is available at `http://localhost:8080`.

## Running with Docker

The `Dockerfile` uses a multi-stage build: Maven builds the jar, and a JRE 25 image runs it as a non-root user.

```bash
docker compose up -d --build
docker compose logs -f app
```

Stop and remove data (destructive):

```bash
docker compose down -v
```

## API Reference

Base URL: `http://localhost:8080/api/products`

| Method | Path | Description | Success |
|---|---|---|---|
| `POST` | `/api/products` | Create one or more products (body: array) | `201` |
| `GET` | `/api/products` | Search products with pagination | `200` |
| `GET` | `/api/products/{parterSKU}` | Get a product by partner SKU | `200` |
| `PUT` | `/api/products/{id}` | Update a product by ID | `200` |
| `POST` | `/api/products/units` | Add units of measure to existing products | `201` |
| `GET` | `/api/products/units` | List all units | `200` |
| `GET` | `/api/products/categories` | List all categories | `200` |
| `POST` | `/api/products/import` | Import products from Excel (`multipart/form-data`, field `file`) | `200` |

### Search parameters

| Parameter | Type | Default | Description |
|---|---|---|---|
| `Keyword` | string | - | Matches SKU or product name |
| `PartnerSKUs` | list | - | Comma-separated partner SKUs |
| `SKUs` | list | - | Comma-separated SKUs |
| `PageIndex` | int | `0` | Zero-based page index |
| `PageSize` | int | `10` | Page size, 1 to 100 |

```bash
curl "http://localhost:8080/api/products?Keyword=iphone&PageIndex=0&PageSize=10"
curl "http://localhost:8080/api/products?SKUs=PHONE010,BUNDLE001"
```

### Create products

`POST /api/products` accepts an array of `ProductRequest`. Minimal example:

```json
[
  {
    "unitCode": "CAI",
    "unitName": "Cái",
    "categories": [
      { "code": "CATEGORY001", "name": "Phone" }
    ],
    "sku": "PHONE010",
    "partnerSKU": "PHONEINTERNAL010",
    "productName": "iPhone 12 test",
    "assetType": "Single",
    "serialType": 1,
    "outboundType": "NotDefined",
    "warehouseCategory": 1,
    "productUnits": [
      {
        "unitCode": "CAI",
        "baseUnitQty": 1,
        "isBaseUnit": true,
        "costPrice": 15000000,
        "sellingPrice": 18000000
      }
    ]
  }
]
```

Required fields: `unitCode`, `sku`, `productName`, `categories` (at least one), `productUnits` (at least one, exactly one with `isBaseUnit: true`).

Response (`201`): a list of `ProductCreateResponse` with `productId`, `sku`, `partnerSKU`, and `status`/`errors` per product. A SKU that already exists is skipped (not an error, but not returned as created).

### Bundles and barcodes

```json
{
  "assetType": "Bundle",
  "productBundles": [
    {
      "sku": "PHONE010",
      "unitCode": "CAI",
      "qty": 1,
      "availablePercentage": 100.0,
      "bundleDetails": [
        { "qty": 1, "salePrice": 199000.5 }
      ]
    }
  ]
}
```

```json
{
  "assetType": "MultipleBarcodes",
  "barcodes": [
    { "barcode": "BC-001-A", "partnerSKU": "PARTNER-A", "name": "Box", "avatarURL": "https://example.com/a.jpg", "refCode": "REF-A" }
  ]
}
```

`salePrice` and `barcode` are unique across the system. A bundle's `sku` must refer to an existing product.

### Update a product

`PUT /api/products/{id}` replaces the product's columns and its child collections. The `sku` cannot be changed. Child collections (units, categories, bundles, barcodes) are removed and recreated from the request.

### Add units of measure

`POST /api/products/units` accepts an array of `ProductUomRequest`:

```bash
curl -X POST http://localhost:8080/api/products/units \
  -H "Content-Type: application/json" \
  -d '[{"sku":"PHONE010","unitCode":"THUNG","baseUnitQty":24,"length":40,"width":30,"height":25,"weight":4800,"costPrice":336000000,"sellingPrice":420000000}]'
```

Errors are reported together: unknown SKUs and unknown unit codes are listed in one message.

### Error responses

Errors use `application/problem+json`:

```json
{
  "status": 409,
  "title": "Conflict",
  "detail": "Key (barcode)=(BC-001-A) already exists."
}
```

| Status | Cause |
|---|---|
| `400` | Validation error, invalid enum value, invalid parameters |
| `404` | Product or resource not found |
| `409` | Unique constraint violation |

## Excel Import

`POST /api/products/import` accepts `.xlsx` or `.xls` files with one sheet per entity.

| Sheet | Required | Columns |
|---|---|---|
| `Products` | yes | `sku`, `partnerSKU`, `productName`, `unitCode`, `unitName`, `assetType`, `serialType`, `outboundType`, `warehouseCategory`, `isTrackingSerial`, `isExpiryDate`, `isLotNumber`, `isBarCode`, `isPhysical`, `color`, `size`, `description`, `hsCode`, `countryOfOrigin`, `taxRate`, `refCode` |
| `Units` | no | `sku`, `unitCode`, `unitName`, `baseUnitQty`, `costPrice`, `sellingPrice`, `weight`, `length`, `width`, `height`, `isBaseUnit`, `isStockConversionEnabled` |
| `Categories` | no | `sku`, `categoryCode`, `categoryName` |
| `Barcodes` | no | `sku`, `barcode`, `partnerSKU`, `name`, `avatarURL`, `refCode` |
| `Bundles` | no | `sku`, `childSku`, `unitCode`, `qty`, `availablePercentage` |
| `BundleDetails` | no | `sku`, `childSku`, `qty`, `salePrice` |

Rules:

- The first row of each sheet is the header. Column order does not matter.
- Child sheets are linked to a product by `sku`.
- Use General or Number formats for numeric cells.
- Dates and currency symbols are not supported in numeric cells.

Sample response:

```json
[
  { "productId": 101, "sku": "IMP001", "partnerSKU": "PARTNERIMP001", "status": true, "errors": [] },
  { "productId": null, "sku": "IMP002", "partnerSKU": "PARTNERIMP002", "status": false,
    "errors": [ { "message": "Dòng 3: ...", "code": "IMPORT_ERROR" } ] }
]
```

## Scheduled Sync Job

It is not yet complete, has not been tested for correct functionality, and the URI field remains empty, view in: /cdms/src/main/java/com/example/cdms/sync/ProductSyncJob.java

## Generating Test Data

`src/test/java/com/example/cdms/tools/ProductApiSeeder.java` generates random products with Datafaker and calls the running API. It is not part of the production build.

Run it from the IDE (Run Configurations, program arguments) or the command line after `./mvnw test-compile`:

```
<count> <sku-prefix> <base-url> <batch-size> [seed]
```

Example:

```
500 FK http://localhost:8080 100
```

Use a different prefix or seed for each run. SKUs and barcodes must be unique.

## Design Notes

- **Two write paths.** Single or small requests use JPA entities and `saveAll`. Large batches use JDBC multi-row inserts with `ON CONFLICT (sku) DO NOTHING` and `RETURNING` to get generated IDs. Child rows are then inserted with JDBC batch updates using the returned product IDs.
- **Transactions.** Service methods are transactional. A failing chunk rolls back all its inserts. The Excel importer retries a failed chunk row by row so that one bad row does not block the rest.
- **Hibernate and JDBC mixing.** JDBC writes bypass the persistence context. After a JDBC insert, reads of the same entities in the same transaction may return stale collections. Use `flush()` and `clear()` before reloading (see `ProductServiceImpl`).
- **Foreign keys use `product_id`.** Child tables reference the product by its primary key. `sku` is a unique business key used for lookups and imports.