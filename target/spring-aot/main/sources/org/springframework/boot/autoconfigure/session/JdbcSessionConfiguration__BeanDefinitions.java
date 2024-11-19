package org.springframework.boot.autoconfigure.session;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.ResolvableType;
import org.springframework.session.config.SessionRepositoryCustomizer;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;

/**
 * Bean definitions for {@link JdbcSessionConfiguration}.
 */
@Generated
public class JdbcSessionConfiguration__BeanDefinitions {
  /**
   * Get the bean definition for 'jdbcSessionConfiguration'.
   */
  public static BeanDefinition getJdbcSessionConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(JdbcSessionConfiguration.class);
    beanDefinition.setInstanceSupplier(JdbcSessionConfiguration::new);
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'jdbcSessionDataSourceScriptDatabaseInitializer'.
   */
  private static BeanInstanceSupplier<JdbcSessionDataSourceScriptDatabaseInitializer> getJdbcSessionDataSourceScriptDatabaseInitializerInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<JdbcSessionDataSourceScriptDatabaseInitializer>forFactoryMethod(JdbcSessionConfiguration.class, "jdbcSessionDataSourceScriptDatabaseInitializer", ObjectProvider.class, ObjectProvider.class, JdbcSessionProperties.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean(JdbcSessionConfiguration.class).jdbcSessionDataSourceScriptDatabaseInitializer(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'jdbcSessionDataSourceScriptDatabaseInitializer'.
   */
  public static BeanDefinition getJdbcSessionDataSourceScriptDatabaseInitializerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(JdbcSessionDataSourceScriptDatabaseInitializer.class);
    beanDefinition.setInstanceSupplier(getJdbcSessionDataSourceScriptDatabaseInitializerInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'springBootSessionRepositoryCustomizer'.
   */
  private static BeanInstanceSupplier<SessionRepositoryCustomizer> getSpringBootSessionRepositoryCustomizerInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<SessionRepositoryCustomizer>forFactoryMethod(JdbcSessionConfiguration.class, "springBootSessionRepositoryCustomizer", SessionProperties.class, JdbcSessionProperties.class, ServerProperties.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean(JdbcSessionConfiguration.class).springBootSessionRepositoryCustomizer(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'springBootSessionRepositoryCustomizer'.
   */
  public static BeanDefinition getSpringBootSessionRepositoryCustomizerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SessionRepositoryCustomizer.class);
    beanDefinition.setTargetType(ResolvableType.forClassWithGenerics(SessionRepositoryCustomizer.class, JdbcIndexedSessionRepository.class));
    beanDefinition.setInstanceSupplier(getSpringBootSessionRepositoryCustomizerInstanceSupplier());
    return beanDefinition;
  }
}
