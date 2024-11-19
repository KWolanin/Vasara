package com.kai.Vasara;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassUtils;

/**
 * Bean definitions for {@link VasaraApplication}.
 */
@Generated
public class VasaraApplication__BeanDefinitions {
  /**
   * Get the bean definition for 'vasaraApplication'.
   */
  public static BeanDefinition getVasaraApplicationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(VasaraApplication.class);
    beanDefinition.setTargetType(VasaraApplication.class);
    ConfigurationClassUtils.initializeConfigurationClass(VasaraApplication.class);
    beanDefinition.setInstanceSupplier(VasaraApplication$$SpringCGLIB$$0::new);
    return beanDefinition;
  }
}
