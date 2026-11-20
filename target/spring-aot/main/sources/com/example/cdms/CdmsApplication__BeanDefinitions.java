package com.example.cdms;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CdmsApplication}.
 */
@Generated
public class CdmsApplication__BeanDefinitions {
  /**
   * Get the bean definition for 'cdmsApplication'.
   */
  public static BeanDefinition getCdmsApplicationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CdmsApplication.class);
    beanDefinition.setInstanceSupplier(CdmsApplication::new);
    return beanDefinition;
  }
}
