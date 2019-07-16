package org.jepria.server.service.rest;

import org.glassfish.jersey.server.ResourceConfig;

public class ApplicationConfigBase extends ResourceConfig {
  
  public ApplicationConfigBase() {
    register(new QueryParamsParamValueFactoryProvider.Binder());
    register(new BodyParamsParamValueFactoryProvider.Binder());
    register(new ExceptionMappers.Validation());
    register(new ExceptionMappers.JsonParse());
  }

}
