package org.springframework.session.jdbc.config.annotation.web.http;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;

/**
 * Bean definitions for {@link JdbcHttpSessionConfiguration}.
 */
@Generated
public class JdbcHttpSessionConfiguration__BeanDefinitions {
  /**
   * Get the bean definition for 'jdbcHttpSessionConfiguration'.
   */
  public static BeanDefinition getJdbcHttpSessionConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(JdbcHttpSessionConfiguration.class);
    InstanceSupplier<JdbcHttpSessionConfiguration> instanceSupplier = InstanceSupplier.using(JdbcHttpSessionConfiguration::new);
    instanceSupplier = instanceSupplier.andThen(JdbcHttpSessionConfiguration__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'sessionRepository'.
   */
  private static BeanInstanceSupplier<JdbcIndexedSessionRepository> getSessionRepositoryInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<JdbcIndexedSessionRepository>forFactoryMethod(JdbcHttpSessionConfiguration.class, "sessionRepository")
            .withGenerator((registeredBean) -> registeredBean.getBeanFactory().getBean(JdbcHttpSessionConfiguration.class).sessionRepository());
  }

  /**
   * Get the bean definition for 'sessionRepository'.
   */
  public static BeanDefinition getSessionRepositoryBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(JdbcIndexedSessionRepository.class);
    beanDefinition.setDependsOn("dataSourceScriptDatabaseInitializer","jdbcSessionDataSourceScriptDatabaseInitializer");
    beanDefinition.setInstanceSupplier(getSessionRepositoryInstanceSupplier());
    return beanDefinition;
  }
}
