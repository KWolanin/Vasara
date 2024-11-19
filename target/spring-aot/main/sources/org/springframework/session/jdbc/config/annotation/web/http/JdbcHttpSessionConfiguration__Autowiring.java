package org.springframework.session.jdbc.config.annotation.web.http;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.aot.AutowiredMethodArgumentsResolver;
import org.springframework.beans.factory.support.RegisteredBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.session.IndexResolver;
import org.springframework.session.SessionIdGenerator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionOperations;

/**
 * Autowiring for {@link JdbcHttpSessionConfiguration}.
 */
@Generated
public class JdbcHttpSessionConfiguration__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static JdbcHttpSessionConfiguration apply(RegisteredBean registeredBean,
      JdbcHttpSessionConfiguration instance) {
    AutowiredMethodArgumentsResolver.forRequiredMethod("setDataSource", ObjectProvider.class, ObjectProvider.class).resolve(registeredBean, args -> instance.setDataSource(args.get(0), args.get(1)));
    AutowiredMethodArgumentsResolver.forMethod("setTransactionManager", PlatformTransactionManager.class).resolve(registeredBean, args -> instance.setTransactionManager(args.get(0)));
    AutowiredMethodArgumentsResolver.forMethod("setTransactionOperations", TransactionOperations.class).resolve(registeredBean, args -> instance.setTransactionOperations(args.get(0)));
    AutowiredMethodArgumentsResolver.forMethod("setIndexResolver", IndexResolver.class).resolve(registeredBean, args -> instance.setIndexResolver(args.get(0)));
    AutowiredMethodArgumentsResolver.forMethod("setLobHandler", LobHandler.class).resolve(registeredBean, args -> instance.setLobHandler(args.get(0)));
    AutowiredMethodArgumentsResolver.forMethod("setSpringSessionConversionService", ConversionService.class).resolve(registeredBean, args -> instance.setSpringSessionConversionService(args.get(0)));
    AutowiredMethodArgumentsResolver.forMethod("setConversionService", ConversionService.class).resolve(registeredBean, args -> instance.setConversionService(args.get(0)));
    AutowiredMethodArgumentsResolver.forMethod("setSessionRepositoryCustomizer", ObjectProvider.class).resolve(registeredBean, args -> instance.setSessionRepositoryCustomizer(args.get(0)));
    AutowiredMethodArgumentsResolver.forMethod("setSessionIdGenerator", SessionIdGenerator.class).resolve(registeredBean, args -> instance.setSessionIdGenerator(args.get(0)));
    return instance;
  }
}
