package org.jepria.server.service.rest.jaxrs.extendedresposne;

import org.glassfish.jersey.server.ContainerRequest;

/*package*/class ExtendedResponseProducerConfiguratorImpl
    implements ExtendedResponse.ProducerConfigurator {

  private ContainerRequest request;
  
  private ExtendedResponse.Factory factory;
  
  public ExtendedResponseProducerConfiguratorImpl() {}

  public ContainerRequest getRequest() {
    return request;
  }

  public void setRequest(ContainerRequest request) {
    this.request = request;
  }

  public ExtendedResponse.Factory getFactory() {
    return factory;
  }

  @Override
  public void setFactory(ExtendedResponse.Factory factory) {
    this.factory = factory;
  }
}
