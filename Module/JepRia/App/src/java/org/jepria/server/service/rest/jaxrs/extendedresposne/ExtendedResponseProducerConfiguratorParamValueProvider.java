package org.jepria.server.service.rest.jaxrs.extendedresposne;

import java.util.function.Function;

import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;

public class ExtendedResponseProducerConfiguratorParamValueProvider implements ValueParamProvider {

  @Override
  public PriorityType getPriority() {
    return Priority.NORMAL;
  }

  @Override
  public Function<ContainerRequest, ?> getValueProvider(Parameter parameter) {
    if (parameter.getRawType() == ExtendedResponse.ProducerConfigurator.class
        && parameter.isAnnotationPresent(ExtendedResponseProducerConfigurator.class)) {
      return containerRequest -> {
        ExtendedResponseProducerConfiguratorImpl configurator =
            new ExtendedResponseProducerConfiguratorImpl();
        configurator.setRequest(containerRequest);
        return configurator;
      };
    }
    // TODO does not work from here: the method is being invoked not only on @ExtendedResponseProducerConfigurator params, but on any params,
    // this results in NULL other param values
    return null;
  }
}