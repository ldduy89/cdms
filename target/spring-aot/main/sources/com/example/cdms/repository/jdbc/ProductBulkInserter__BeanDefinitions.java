package com.example.cdms.repository.jdbc;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Bean definitions for {@link ProductBulkInserter}.
 */
@Generated
public class ProductBulkInserter__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'productBulkInserter'.
   */
  private static BeanInstanceSupplier<ProductBulkInserter> getProductBulkInserterInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<ProductBulkInserter>forConstructor(JdbcTemplate.class)
            .withGenerator((registeredBean, args) -> new ProductBulkInserter(args.get(0)));
  }

  /**
   * Get the bean definition for 'productBulkInserter'.
   */
  public static BeanDefinition getProductBulkInserterBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ProductBulkInserter.class);
    beanDefinition.setInstanceSupplier(getProductBulkInserterInstanceSupplier());
    return beanDefinition;
  }
}
