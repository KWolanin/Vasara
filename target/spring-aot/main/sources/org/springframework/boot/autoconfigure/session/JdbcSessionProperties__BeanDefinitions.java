package org.springframework.boot.autoconfigure.session;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link JdbcSessionProperties}.
 */
@Generated
public class JdbcSessionProperties__BeanDefinitions {
  /**
   * Get the bean definition for 'jdbcSessionProperties'.
   */
  public static BeanDefinition getJdbcSessionPropertiesBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(JdbcSessionProperties.class);
    beanDefinition.setInstanceSupplier(JdbcSessionProperties::new);
    return beanDefinition;
  }
}
