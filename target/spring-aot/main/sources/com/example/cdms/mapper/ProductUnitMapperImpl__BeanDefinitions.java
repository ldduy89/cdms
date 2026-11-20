package com.example.cdms.mapper;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ProductUnitMapperImpl}.
 */
@Generated
public class ProductUnitMapperImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'productUnitMapperImpl'.
   */
  public static BeanDefinition getProductUnitMapperImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ProductUnitMapperImpl.class);
    beanDefinition.setInstanceSupplier(ProductUnitMapperImpl::new);
    return beanDefinition;
  }
}
