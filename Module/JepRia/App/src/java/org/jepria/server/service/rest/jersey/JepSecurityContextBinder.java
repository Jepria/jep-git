package org.jepria.server.service.rest.jersey;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.jepria.server.service.security.JepSecurityContext;

public class JepSecurityContextBinder extends AbstractBinder {
  @Override
  protected void configure() {
    bindFactory(JepSecurityContextFactory.class).to(JepSecurityContext.class);
  }
}
