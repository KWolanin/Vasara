package org.springframework.session.config.annotation.web.http;

import jakarta.servlet.ServletContext;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredMethodArgumentsResolver;
import org.springframework.beans.factory.support.RegisteredBean;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**
 * Autowiring for {@link SpringHttpSessionConfiguration}.
 */
@Generated
public class SpringHttpSessionConfiguration__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static SpringHttpSessionConfiguration apply(RegisteredBean registeredBean,
      SpringHttpSessionConfiguration instance) {
    AutowiredMethodArgumentsResolver.forMethod("setServletContext", ServletContext.class).resolve(registeredBean, args -> instance.setServletContext(args.get(0)));
    AutowiredMethodArgumentsResolver.forMethod("setCookieSerializer", CookieSerializer.class).resolve(registeredBean, args -> instance.setCookieSerializer(args.get(0)));
    AutowiredMethodArgumentsResolver.forMethod("setHttpSessionIdResolver", HttpSessionIdResolver.class).resolve(registeredBean, args -> instance.setHttpSessionIdResolver(args.get(0)));
    AutowiredMethodArgumentsResolver.forMethod("setHttpSessionListeners", List.class).resolve(registeredBean, args -> instance.setHttpSessionListeners(args.get(0)));
    return instance;
  }
}
