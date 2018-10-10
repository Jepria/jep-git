package org.jepria.server.service.rest;

import org.glassfish.jersey.server.ResourceConfig;

public class BaseApplicationConfig extends ResourceConfig {
  
  public BaseApplicationConfig() {
    packages("io.swagger.jaxrs.listing");
  }

}
