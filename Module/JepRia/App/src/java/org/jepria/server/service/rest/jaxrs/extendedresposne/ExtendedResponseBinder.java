package org.jepria.server.service.rest.jaxrs.extendedresposne;

import javax.inject.Singleton;

import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;

public class ExtendedResponseBinder extends AbstractBinder {
  @Override
  protected void configure() {
      bind(ExtendedResponseProducerConfiguratorParamResolver.class)
      .to(new TypeLiteral<InjectionResolver<ExtendedResponseProducerConfigurator>>(){})
      .in(Singleton.class);
      bind(ExtendedResponseProducerConfiguratorParamValueProvider.class)
      .to(ValueParamProvider.class)
      .in(Singleton.class);
      bind(ExtendedResponseInterceptionService.class)
      .to(org.glassfish.hk2.api.InterceptionService.class)
      .in(Singleton.class);
  }
}