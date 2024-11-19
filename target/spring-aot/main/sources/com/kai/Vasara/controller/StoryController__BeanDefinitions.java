package com.kai.Vasara.controller;

import com.kai.Vasara.service.StoryService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link StoryController}.
 */
@Generated
public class StoryController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'storyController'.
   */
  private static BeanInstanceSupplier<StoryController> getStoryControllerInstanceSupplier() {
    return BeanInstanceSupplier.<StoryController>forConstructor(StoryService.class)
            .withGenerator((registeredBean, args) -> new StoryController(args.get(0)));
  }

  /**
   * Get the bean definition for 'storyController'.
   */
  public static BeanDefinition getStoryControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(StoryController.class);
    beanDefinition.setInstanceSupplier(getStoryControllerInstanceSupplier());
    return beanDefinition;
  }
}
