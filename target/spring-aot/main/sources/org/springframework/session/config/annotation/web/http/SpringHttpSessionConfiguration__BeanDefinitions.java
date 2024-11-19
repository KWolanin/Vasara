package org.springframework.session.config.annotation.web.http;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.ResolvableType;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.SessionEventHttpSessionListenerAdapter;
import org.springframework.session.web.http.SessionRepositoryFilter;

/**
 * Bean definitions for {@link SpringHttpSessionConfiguration}.
 */
@Generated
public class SpringHttpSessionConfiguration__BeanDefinitions {
  /**
   * Get the bean definition for 'springHttpSessionConfiguration'.
   */
  public static BeanDefinition getSpringHttpSessionConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SpringHttpSessionConfiguration.class);
    InstanceSupplier<SpringHttpSessionConfiguration> instanceSupplier = InstanceSupplier.using(SpringHttpSessionConfiguration::new);
    instanceSupplier = instanceSupplier.andThen(SpringHttpSessionConfiguration__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'sessionEventHttpSessionListenerAdapter'.
   */
  private static BeanInstanceSupplier<SessionEventHttpSessionListenerAdapter> getSessionEventHttpSessionListenerAdapterInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<SessionEventHttpSessionListenerAdapter>forFactoryMethod(SpringHttpSessionConfiguration.class, "sessionEventHttpSessionListenerAdapter")
            .withGenerator((registeredBean) -> registeredBean.getBeanFactory().getBean(SpringHttpSessionConfiguration.class).sessionEventHttpSessionListenerAdapter());
  }

  /**
   * Get the bean definition for 'sessionEventHttpSessionListenerAdapter'.
   */
  public static BeanDefinition getSessionEventHttpSessionListenerAdapterBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SessionEventHttpSessionListenerAdapter.class);
    beanDefinition.setInstanceSupplier(getSessionEventHttpSessionListenerAdapterInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'springSessionRepositoryFilter'.
   */
  private static BeanInstanceSupplier<SessionRepositoryFilter> getSpringSessionRepositoryFilterInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<SessionRepositoryFilter>forFactoryMethod(SpringHttpSessionConfiguration.class, "springSessionRepositoryFilter", SessionRepository.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean(SpringHttpSessionConfiguration.class).springSessionRepositoryFilter(args.get(0)));
  }

  /**
   * Get the bean definition for 'springSessionRepositoryFilter'.
   */
  public static BeanDefinition getSpringSessionRepositoryFilterBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SessionRepositoryFilter.class);
    beanDefinition.setTargetType(ResolvableType.forClassWithGenerics(SessionRepositoryFilter.class, Session.class));
    beanDefinition.setInstanceSupplier(getSpringSessionRepositoryFilterInstanceSupplier());
    return beanDefinition;
  }
}
