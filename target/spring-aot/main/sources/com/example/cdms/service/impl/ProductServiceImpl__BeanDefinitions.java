package com.example.cdms.service.impl;

import com.example.cdms.mapper.ProductCategoryMapper;
import com.example.cdms.mapper.ProductMapper;
import com.example.cdms.mapper.ProductUnitMapper;
import com.example.cdms.repository.ProductBarcodeRepository;
import com.example.cdms.repository.ProductCategoryRepository;
import com.example.cdms.repository.ProductRepository;
import com.example.cdms.repository.ProductUnitRepository;
import com.example.cdms.repository.jdbc.ProductBulkInserter;
import com.example.cdms.service.ProductCategoryService;
import com.example.cdms.service.ProductUnitService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ProductServiceImpl}.
 */
@Generated
public class ProductServiceImpl__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'productServiceImpl'.
   */
  private static BeanInstanceSupplier<ProductServiceImpl> getProductServiceImplInstanceSupplier() {
    return BeanInstanceSupplier.<ProductServiceImpl>forConstructor(ProductRepository.class, ProductUnitRepository.class, ProductCategoryRepository.class, ProductBarcodeRepository.class, ProductMapper.class, ProductUnitMapper.class, ProductCategoryMapper.class, ProductBulkInserter.class, ProductUnitService.class, ProductCategoryService.class)
            .withGenerator((registeredBean, args) -> new ProductServiceImpl(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4), args.get(5), args.get(6), args.get(7), args.get(8), args.get(9)));
  }

  /**
   * Get the bean definition for 'productServiceImpl'.
   */
  public static BeanDefinition getProductServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ProductServiceImpl.class);
    InstanceSupplier<ProductServiceImpl> instanceSupplier = getProductServiceImplInstanceSupplier();
    instanceSupplier = instanceSupplier.andThen(ProductServiceImpl__PersistenceInjection::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
