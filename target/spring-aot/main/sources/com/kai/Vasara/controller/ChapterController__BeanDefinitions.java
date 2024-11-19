package com.kai.Vasara.controller;

import com.kai.Vasara.service.ChapterService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ChapterController}.
 */
@Generated
public class ChapterController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'chapterController'.
   */
  private static BeanInstanceSupplier<ChapterController> getChapterControllerInstanceSupplier() {
    return BeanInstanceSupplier.<ChapterController>forConstructor(ChapterService.class)
            .withGenerator((registeredBean, args) -> new ChapterController(args.get(0)));
  }

  /**
   * Get the bean definition for 'chapterController'.
   */
  public static BeanDefinition getChapterControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ChapterController.class);
    beanDefinition.setInstanceSupplier(getChapterControllerInstanceSupplier());
    return beanDefinition;
  }
}
