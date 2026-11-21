package com.example.cdms.repository;

import com.example.cdms.entity.ProductUnit;
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
 * AOT generated JPA repository implementation for {@link ProductUnitRepository}.
 */
@Generated
public class ProductUnitRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ProductUnitRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ProductUnitRepository#findByCode(java.lang.String)}.
   */
  public Optional<ProductUnit> findByCode(String code) {
    String queryString = "SELECT p FROM ProductUnit p WHERE p.code = :code";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("code", code);

    return Optional.ofNullable((ProductUnit) convertOne(query.getSingleResultOrNull(), false, ProductUnit.class));
  }

  /**
   * AOT generated implementation of {@link ProductUnitRepository#findByCodeIn(java.util.Collection)}.
   */
  public List<ProductUnit> findByCodeIn(Collection<String> codes) {
    String queryString = "SELECT p FROM ProductUnit p WHERE p.code IN :codes";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("codes", codes);

    return (List<ProductUnit>) query.getResultList();
  }
}
