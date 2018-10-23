package org.jepria.server.service.rest.jaxrs;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 * Class for extending response body with arbitrary entity.
 * <br/>
 * Example:
 * <br/>
 * Response body before extension:
 * <pre>
 * {
 *   "key": "value"
 * }
 * </pre>
 * Same response extended with the code 
 * <pre>ExtendedResponse.from(response).extend("{ 'extKey': 'extvalue' }").asResponse();</pre>
 * <pre>
 * { 
 *   "basic-response":
 *     {
 *       "key": "value"
 *     },
 *   "extended-response": 
 *     {
 *       "extKey": "extValue"
 *     }
 * }
 * </pre>
 * 
 */
public final class ExtendedResponse {
 
  /**
   * The response under extension
   */
  private Response response;
  
  private ExtendedResponse(Response response) {
    this.response = response;
  }
  
  /**
   * Performs extension on the {@link #response}
   */
  public static interface Extender {
    /**
     * Performs extension on the underlying {@link #response}
     * @param extendedEntity entity to put under the 'extended-response' response body key
     * @return extended response
     */
    ExtendedResponse extend(Object extendedEntity);
  }
  
  /**
   * @param responseBuilder
   * @return
   */
  public static Extender from(ResponseBuilder responseBuilder) {
    if (responseBuilder == null) {
      throw new IllegalArgumentException("responseBuilder must not be null");
    }
    
    return from(responseBuilder.build());
  }
  
  public static Extender from(Response response) {
    if (response == null) {
      throw new IllegalArgumentException("response must not be null");
    }
    
    return new ExtendedResponse(response).new ExtenderImpl();
  }
  
  private final class ExtenderImpl implements Extender {
    
    @Override
    public ExtendedResponse extend(Object extendedEntity) {
      Object basicEntityPart = response.getEntity();
      
      Map<String, Object> newEntity = new HashMap<>();
      if (basicEntityPart != null) {
        newEntity.put("basic-response", basicEntityPart);
      }
      newEntity.put("extended-response", extendedEntity);
      
      final ResponseBuilder builder = Response.fromResponse(response);
      builder.entity(newEntity);
      
      response = builder.build();
      
      return ExtendedResponse.this;
    }
    
  }
  
  
    
  public Response asResponse() {
    return response;
  }
  
  public ResponseBuilder asBuilder() {
    return Response.fromResponse(response);
  }
}
