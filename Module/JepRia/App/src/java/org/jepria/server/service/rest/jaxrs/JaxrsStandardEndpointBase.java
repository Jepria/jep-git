package org.jepria.server.service.rest.jaxrs;

import java.util.function.Supplier;

import org.jepria.server.service.rest.Credential;
import org.jepria.server.service.rest.StandardResourceDescription;
import org.jepria.server.service.rest.StandardResourceController;
import org.jepria.server.service.rest.StandardResourceControllerImpl;

public abstract class JaxrsStandardEndpointBase {

  /**
   * Provides application resource description
   * @return
   */
  protected abstract StandardResourceDescription getResourceDescription();
  
  protected abstract Credential getCredential();
  
  /**
   * Supplier protects the internal field from direct access from within the class members,
   * and initializes the field lazily (due to the DI: the injectable fields are being injected after the object construction)
   */
  protected final Supplier<StandardResourceController> resourceController = new Supplier<StandardResourceController>() {
    private StandardResourceController instance = null;
    @Override
    public StandardResourceController get() {
      if (instance == null) {
        instance = createResourceController();
      }
      return instance;
    }
  };
  
  protected StandardResourceController createResourceController() {
    return new StandardResourceControllerImpl(getResourceDescription());
  }
}
