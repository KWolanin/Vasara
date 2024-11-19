package com.kai.Vasara.service;

import com.kai.Vasara.repository.StoryRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link StoryService}.
 */
@Generated
public class StoryService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'storyService'.
   */
  private static BeanInstanceSupplier<StoryService> getStoryServiceInstanceSupplier() {
    return BeanInstanceSupplier.<StoryService>forConstructor(StoryRepository.class, AuthorService.class, ChapterService.class)
            .withGenerator((registeredBean, args) -> new StoryService(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'storyService'.
   */
  public static BeanDefinition getStoryServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(StoryService.class);
    beanDefinition.setInstanceSupplier(getStoryServiceInstanceSupplier());
    return beanDefinition;
  }
}
