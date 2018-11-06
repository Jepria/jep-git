package org.jepria.server.service.rest;

import org.glassfish.jersey.server.ResourceConfig;
import org.jepria.server.service.rest.jaxrs.extendedresposne.ExtendedResponseBinder;

public class BaseApplicationConfig extends ResourceConfig {
  
  public BaseApplicationConfig() {
    packages("io.swagger.jaxrs.listing");
    register(new ExtendedResponseBinder());
  }

}
