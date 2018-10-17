package org.jepria.server.service.rest.jaxrs;

import java.util.List;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jepria.server.service.rest.Credential;
import org.jepria.server.service.rest.ResourceDescription;
import org.jepria.server.service.rest.StandardResourceController;
import org.jepria.server.service.rest.StandardResourceControllerImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Standard Jaxrs REST resource for light-weight options on the application level
 */
@Path("/option")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Api
public abstract class JaxrsApplicationOptionEndpoint {
  
  protected JaxrsApplicationOptionEndpoint() {}
  
  /**
   * Injectable field
   */
  @Context 
  private HttpServletRequest request;
  
  /**
   * Provides application resource description
   * @return
   */
  protected abstract ResourceDescription getResourceDescription();
  
  /**
   * Get credential from the request
   * @return
   */
  protected Credential getCredential() {
    Integer operatorId = 1;
    // TODO = (Integer)request.getHeader("operatorId");
    return new Credential() {
      @Override
      public Integer getOperatorId() {
        return operatorId;
      }
    };
  }
  
  /**
   * Supplier protects the internal field from direct access from within the class members,
   * and initializes the field lazily (due to the DI: the injectable fields are being injected after the object construction)
   */
  private final Supplier<StandardResourceController> controller = new Supplier<StandardResourceController>() {
    private StandardResourceController instance = null;
    @Override
    public StandardResourceController get() {
      if (instance == null) {
        instance = createStandardResourceController();
      }
      return instance;
    }
  };
  
  protected StandardResourceController createStandardResourceController() {
    return new StandardResourceControllerImpl(getResourceDescription().getRecordDefinition(), getResourceDescription().getDao());
  }
  
  //////// OPTIONS ////////
  
  @GET
  @Path("{optionEntityName}")
  @ApiOperation(value = "List options by option-entity name")
  public Response listOptions(@PathParam("optionEntityName") String optionEntityName) {
    List<?> result = controller.get().listOptions(optionEntityName, getCredential());
    if (result == null || result.isEmpty()) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(result).build();
    }
  }
}