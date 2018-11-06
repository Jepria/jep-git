package org.jepria.server.service.rest.jaxrs.extendedresposne;

import javax.inject.Inject;
import javax.inject.Named;

import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;

public class ExtendedResponseProducerConfiguratorParamResolver 
    implements InjectionResolver<ExtendedResponse.ProducerConfigurator> {
  
  @Inject
  @Named(InjectionResolver.SYSTEM_RESOLVER_NAME)
  InjectionResolver<Inject> systemInjectionResolver;

  @Override
  public Object resolve(Injectee injectee, ServiceHandle<?> root) {
      if (ExtendedResponseProducerConfigurator.class == injectee.getRequiredType()) {
          return systemInjectionResolver.resolve(injectee, root);
      }
      return null;
  }

  @Override
  public boolean isConstructorParameterIndicator() { return false; }

  @Override
  public boolean isMethodParameterIndicator() { return false; }   
}
