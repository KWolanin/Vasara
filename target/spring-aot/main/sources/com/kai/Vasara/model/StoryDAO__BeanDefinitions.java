package com.kai.Vasara.model;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link StoryDAO}.
 */
@Generated
public class StoryDAO__BeanDefinitions {
  /**
   * Get the bean definition for 'storyDAO'.
   */
  public static BeanDefinition getStoryDAOBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(StoryDAO.class);
    InstanceSupplier<StoryDAO> instanceSupplier = InstanceSupplier.using(StoryDAO::new);
    instanceSupplier = instanceSupplier.andThen(StoryDAO__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
