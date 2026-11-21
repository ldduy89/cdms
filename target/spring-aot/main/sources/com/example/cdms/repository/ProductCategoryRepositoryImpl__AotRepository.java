package com.example.cdms.repository;

import com.example.cdms.entity.ProductCategory;
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
 * AOT generated JPA repository implementation for {@link ProductCategoryRepository}.
 */
@Generated
public class ProductCategoryRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ProductCategoryRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ProductCategoryRepository#findByCode(java.lang.String)}.
   */
  public Optional<ProductCategory> findByCode(String code) {
    String queryString = "SELECT p FROM ProductCategory p WHERE p.code = :code";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("code", code);

    return Optional.ofNullable((ProductCategory) convertOne(query.getSingleResultOrNull(), false, ProductCategory.class));
  }

  /**
   * AOT generated implementation of {@link ProductCategoryRepository#findByCodeIn(java.util.Collection)}.
   */
  public List<ProductCategory> findByCodeIn(Collection<String> codes) {
    String queryString = "SELECT p FROM ProductCategory p WHERE p.code IN :codes";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("codes", codes);

    return (List<ProductCategory>) query.getResultList();
  }
}
