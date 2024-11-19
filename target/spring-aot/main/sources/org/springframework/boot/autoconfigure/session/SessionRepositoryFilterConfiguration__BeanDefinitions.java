package org.springframework.boot.autoconfigure.session;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;

/**
 * Bean definitions for {@link SessionRepositoryFilterConfiguration}.
 */
@Generated
public class SessionRepositoryFilterConfiguration__BeanDefinitions {
  /**
   * Get the bean definition for 'sessionRepositoryFilterConfiguration'.
   */
  public static BeanDefinition getSessionRepositoryFilterConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SessionRepositoryFilterConfiguration.class);
    beanDefinition.setInstanceSupplier(SessionRepositoryFilterConfiguration::new);
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'sessionRepositoryFilterRegistration'.
   */
  private static BeanInstanceSupplier<DelegatingFilterProxyRegistrationBean> getSessionRepositoryFilterRegistrationInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<DelegatingFilterProxyRegistrationBean>forFactoryMethod(SessionRepositoryFilterConfiguration.class, "sessionRepositoryFilterRegistration", SessionProperties.class, ListableBeanFactory.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean(SessionRepositoryFilterConfiguration.class).sessionRepositoryFilterRegistration(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'sessionRepositoryFilterRegistration'.
   */
  public static BeanDefinition getSessionRepositoryFilterRegistrationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(DelegatingFilterProxyRegistrationBean.class);
    beanDefinition.setInstanceSupplier(getSessionRepositoryFilterRegistrationInstanceSupplier());
    return beanDefinition;
  }
}
