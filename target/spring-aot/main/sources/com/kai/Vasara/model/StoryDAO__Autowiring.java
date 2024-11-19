package com.kai.Vasara.model;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link StoryDAO}.
 */
@Generated
public class StoryDAO__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static StoryDAO apply(RegisteredBean registeredBean, StoryDAO instance) {
    AutowiredFieldValueResolver.forRequiredField("authorService").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("chapterService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
