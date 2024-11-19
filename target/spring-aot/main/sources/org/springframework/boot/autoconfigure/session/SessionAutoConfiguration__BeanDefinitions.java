package org.springframework.boot.autoconfigure.session;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * Bean definitions for {@link SessionAutoConfiguration}.
 */
@Generated
public class SessionAutoConfiguration__BeanDefinitions {
  /**
   * Get the bean definition for 'sessionAutoConfiguration'.
   */
  public static BeanDefinition getSessionAutoConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SessionAutoConfiguration.class);
    beanDefinition.setInstanceSupplier(SessionAutoConfiguration::new);
    return beanDefinition;
  }

  /**
   * Bean definitions for {@link SessionAutoConfiguration.ServletSessionConfiguration}.
   */
  @Generated
  public static class ServletSessionConfiguration {
    /**
     * Get the bean definition for 'servletSessionConfiguration'.
     */
    public static BeanDefinition getServletSessionConfigurationBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(SessionAutoConfiguration.ServletSessionConfiguration.class);
      beanDefinition.setInstanceSupplier(SessionAutoConfiguration.ServletSessionConfiguration::new);
      return beanDefinition;
    }

    /**
     * Get the bean instance supplier for 'cookieSerializer'.
     */
    private static BeanInstanceSupplier<DefaultCookieSerializer> getCookieSerializerInstanceSupplier(
        ) {
      return BeanInstanceSupplier.<DefaultCookieSerializer>forFactoryMethod(SessionAutoConfiguration.ServletSessionConfiguration.class, "cookieSerializer", ServerProperties.class, ObjectProvider.class)
              .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean(SessionAutoConfiguration.ServletSessionConfiguration.class).cookieSerializer(args.get(0), args.get(1)));
    }

    /**
     * Get the bean definition for 'cookieSerializer'.
     */
    public static BeanDefinition getCookieSerializerBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(DefaultCookieSerializer.class);
      beanDefinition.setInstanceSupplier(getCookieSerializerInstanceSupplier());
      return beanDefinition;
    }

    /**
     * Bean definitions for {@link SessionAutoConfiguration.ServletSessionConfiguration.ServletSessionRepositoryConfiguration}.
     */
    @Generated
    public static class ServletSessionRepositoryConfiguration {
      /**
       * Get the bean definition for 'servletSessionRepositoryConfiguration'.
       */
      public static BeanDefinition getServletSessionRepositoryConfigurationBeanDefinition() {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(SessionAutoConfiguration.ServletSessionConfiguration.ServletSessionRepositoryConfiguration.class);
        beanDefinition.setInstanceSupplier(SessionAutoConfiguration.ServletSessionConfiguration.ServletSessionRepositoryConfiguration::new);
        return beanDefinition;
      }
    }

    /**
     * Bean definitions for {@link SessionAutoConfiguration.ServletSessionConfiguration.RememberMeServicesConfiguration}.
     */
    @Generated
    public static class RememberMeServicesConfiguration {
      /**
       * Get the bean definition for 'rememberMeServicesConfiguration'.
       */
      public static BeanDefinition getRememberMeServicesConfigurationBeanDefinition() {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(SessionAutoConfiguration.ServletSessionConfiguration.RememberMeServicesConfiguration.class);
        beanDefinition.setInstanceSupplier(SessionAutoConfiguration.ServletSessionConfiguration.RememberMeServicesConfiguration::new);
        return beanDefinition;
      }

      /**
       * Get the bean instance supplier for 'rememberMeServicesCookieSerializerCustomizer'.
       */
      private static BeanInstanceSupplier<DefaultCookieSerializerCustomizer> getRememberMeServicesCookieSerializerCustomizerInstanceSupplier(
          ) {
        return BeanInstanceSupplier.<DefaultCookieSerializerCustomizer>forFactoryMethod(SessionAutoConfiguration.ServletSessionConfiguration.RememberMeServicesConfiguration.class, "rememberMeServicesCookieSerializerCustomizer")
                .withGenerator((registeredBean) -> registeredBean.getBeanFactory().getBean(SessionAutoConfiguration.ServletSessionConfiguration.RememberMeServicesConfiguration.class).rememberMeServicesCookieSerializerCustomizer());
      }

      /**
       * Get the bean definition for 'rememberMeServicesCookieSerializerCustomizer'.
       */
      public static BeanDefinition getRememberMeServicesCookieSerializerCustomizerBeanDefinition() {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(DefaultCookieSerializerCustomizer.class);
        beanDefinition.setInstanceSupplier(getRememberMeServicesCookieSerializerCustomizerInstanceSupplier());
        return beanDefinition;
      }
    }
  }
}
