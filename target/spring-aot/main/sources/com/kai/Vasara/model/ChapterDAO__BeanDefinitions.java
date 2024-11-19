package com.kai.Vasara.model;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ChapterDAO}.
 */
@Generated
public class ChapterDAO__BeanDefinitions {
  /**
   * Get the bean definition for 'chapterDAO'.
   */
  public static BeanDefinition getChapterDAOBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ChapterDAO.class);
    beanDefinition.setInstanceSupplier(ChapterDAO::new);
    return beanDefinition;
  }
}
