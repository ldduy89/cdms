package com.example.cdms.repository;

import com.example.cdms.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.String;
import java.util.Optional;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link ProductRepository}.
 */
@Generated
public class ProductRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ProductRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ProductRepository#existsBySku(java.lang.String)}.
   */
  public boolean existsBySku(String sku) {
    String queryString = "SELECT p.productId FROM Product p WHERE p.sku = :sku";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("sku", sku);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link ProductRepository#findByPartnerSKU(java.lang.String)}.
   */
  public Optional<Product> findByPartnerSKU(String partnerSKU) {
    String queryString = "SELECT p FROM Product p WHERE p.partnerSKU = :partnerSKU";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("partnerSKU", partnerSKU);

    return Optional.ofNullable((Product) convertOne(query.getSingleResultOrNull(), false, Product.class));
  }
}
