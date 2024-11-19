package org.springframework.boot.autoconfigure.security.servlet;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Bean definitions for {@link SpringBootWebSecurityConfiguration}.
 */
@Generated
public class SpringBootWebSecurityConfiguration__BeanDefinitions {
  /**
   * Get the bean definition for 'springBootWebSecurityConfiguration'.
   */
  public static BeanDefinition getSpringBootWebSecurityConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SpringBootWebSecurityConfiguration.class);
    beanDefinition.setInstanceSupplier(SpringBootWebSecurityConfiguration::new);
    return beanDefinition;
  }

  /**
   * Bean definitions for {@link SpringBootWebSecurityConfiguration.WebSecurityEnablerConfiguration}.
   */
  @Generated
  public static class WebSecurityEnablerConfiguration {
    /**
     * Get the bean definition for 'webSecurityEnablerConfiguration'.
     */
    public static BeanDefinition getWebSecurityEnablerConfigurationBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(SpringBootWebSecurityConfiguration.WebSecurityEnablerConfiguration.class);
      beanDefinition.setInstanceSupplier(SpringBootWebSecurityConfiguration.WebSecurityEnablerConfiguration::new);
      return beanDefinition;
    }
  }

  /**
   * Bean definitions for {@link SpringBootWebSecurityConfiguration.SecurityFilterChainConfiguration}.
   */
  @Generated
  public static class SecurityFilterChainConfiguration {
    /**
     * Get the bean definition for 'securityFilterChainConfiguration'.
     */
    public static BeanDefinition getSecurityFilterChainConfigurationBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(SpringBootWebSecurityConfiguration.SecurityFilterChainConfiguration.class);
      beanDefinition.setInstanceSupplier(SpringBootWebSecurityConfiguration.SecurityFilterChainConfiguration::new);
      return beanDefinition;
    }

    /**
     * Get the bean instance supplier for 'defaultSecurityFilterChain'.
     */
    private static BeanInstanceSupplier<SecurityFilterChain> getDefaultSecurityFilterChainInstanceSupplier(
        ) {
      return BeanInstanceSupplier.<SecurityFilterChain>forFactoryMethod(SpringBootWebSecurityConfiguration.SecurityFilterChainConfiguration.class, "defaultSecurityFilterChain", HttpSecurity.class)
              .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean(SpringBootWebSecurityConfiguration.SecurityFilterChainConfiguration.class).defaultSecurityFilterChain(args.get(0)));
    }

    /**
     * Get the bean definition for 'defaultSecurityFilterChain'.
     */
    public static BeanDefinition getDefaultSecurityFilterChainBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(SecurityFilterChain.class);
      beanDefinition.setInstanceSupplier(getDefaultSecurityFilterChainInstanceSupplier());
      return beanDefinition;
    }
  }
}
