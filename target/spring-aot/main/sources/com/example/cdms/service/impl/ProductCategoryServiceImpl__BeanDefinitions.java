package com.example.cdms.service.impl;

import com.example.cdms.repository.ProductCategoryRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ProductCategoryServiceImpl}.
 */
@Generated
public class ProductCategoryServiceImpl__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'productCategoryServiceImpl'.
   */
  private static BeanInstanceSupplier<ProductCategoryServiceImpl> getProductCategoryServiceImplInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<ProductCategoryServiceImpl>forConstructor(ProductCategoryRepository.class)
            .withGenerator((registeredBean, args) -> new ProductCategoryServiceImpl(args.get(0)));
  }

  /**
   * Get the bean definition for 'productCategoryServiceImpl'.
   */
  public static BeanDefinition getProductCategoryServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ProductCategoryServiceImpl.class);
    beanDefinition.setInstanceSupplier(getProductCategoryServiceImplInstanceSupplier());
    return beanDefinition;
  }
}
