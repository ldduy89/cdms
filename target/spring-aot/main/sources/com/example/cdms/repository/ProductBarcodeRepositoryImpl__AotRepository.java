package com.example.cdms.repository;

import com.example.cdms.entity.ProductBarcode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.String;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link ProductBarcodeRepository}.
 */
@Generated
public class ProductBarcodeRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ProductBarcodeRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ProductBarcodeRepository#findByBarcode(java.lang.String)}.
   */
  public Optional<ProductBarcode> findByBarcode(String barcode) {
    String queryString = "SELECT p FROM ProductBarcode p WHERE p.barcode = :barcode";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("barcode", barcode);

    return Optional.ofNullable((ProductBarcode) convertOne(query.getSingleResultOrNull(), false, ProductBarcode.class));
  }

  /**
   * AOT generated implementation of {@link ProductBarcodeRepository#findByBarcodeIn(java.util.Collection)}.
   */
  public List<ProductBarcode> findByBarcodeIn(Collection<String> barcodes) {
    String queryString = "SELECT p FROM ProductBarcode p WHERE p.barcode IN :barcodes";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("barcodes", barcodes);

    return (List<ProductBarcode>) query.getResultList();
  }
}
