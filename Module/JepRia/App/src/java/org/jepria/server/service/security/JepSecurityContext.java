package org.jepria.server.service.security;

import javax.ws.rs.core.SecurityContext;

/**
 * Расширенный {@code SecurityContext} для инъекций в Jaxrs-ресурсы
 */
public interface JepSecurityContext extends SecurityContext {
  Credential getCredential();
}
