package com.example.cdms.service.impl;

import com.example.cdms.repository.ProductUnitRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ProductUnitServiceImpl}.
 */
@Generated
public class ProductUnitServiceImpl__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'productUnitServiceImpl'.
   */
  private static BeanInstanceSupplier<ProductUnitServiceImpl> getProductUnitServiceImplInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<ProductUnitServiceImpl>forConstructor(ProductUnitRepository.class)
            .withGenerator((registeredBean, args) -> new ProductUnitServiceImpl(args.get(0)));
  }

  /**
   * Get the bean definition for 'productUnitServiceImpl'.
   */
  public static BeanDefinition getProductUnitServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ProductUnitServiceImpl.class);
    beanDefinition.setInstanceSupplier(getProductUnitServiceImplInstanceSupplier());
    return beanDefinition;
  }
}
