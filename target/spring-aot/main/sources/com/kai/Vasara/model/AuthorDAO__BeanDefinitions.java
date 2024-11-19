package com.kai.Vasara.model;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link AuthorDAO}.
 */
@Generated
public class AuthorDAO__BeanDefinitions {
  /**
   * Get the bean definition for 'authorDAO'.
   */
  public static BeanDefinition getAuthorDAOBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(AuthorDAO.class);
    beanDefinition.setInstanceSupplier(AuthorDAO::new);
    return beanDefinition;
  }
}
