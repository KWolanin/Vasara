package org.springframework.boot.autoconfigure.session;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link SessionProperties}.
 */
@Generated
public class SessionProperties__BeanDefinitions {
  /**
   * Get the bean definition for 'sessionProperties'.
   */
  public static BeanDefinition getSessionPropertiesBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SessionProperties.class);
    beanDefinition.setInstanceSupplier(SessionProperties::new);
    return beanDefinition;
  }
}
