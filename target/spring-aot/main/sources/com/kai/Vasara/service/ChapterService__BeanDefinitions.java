package com.kai.Vasara.service;

import com.kai.Vasara.repository.ChapterRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ChapterService}.
 */
@Generated
public class ChapterService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'chapterService'.
   */
  private static BeanInstanceSupplier<ChapterService> getChapterServiceInstanceSupplier() {
    return BeanInstanceSupplier.<ChapterService>forConstructor(ChapterRepository.class)
            .withGenerator((registeredBean, args) -> new ChapterService(args.get(0)));
  }

  /**
   * Get the bean definition for 'chapterService'.
   */
  public static BeanDefinition getChapterServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ChapterService.class);
    beanDefinition.setInstanceSupplier(getChapterServiceInstanceSupplier());
    return beanDefinition;
  }
}
