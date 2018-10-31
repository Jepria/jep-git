package org.jepria.server.service.rest.jaxrs;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
 * <pre>ExtendedResponse.from(response).extend("{ 'extKey': 'extValue' }").asResponse();</pre>
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
public class ExtendedResponseHeaderProcessor {
  
  public static final String HEADER_NAME__EXTENDED_RESPONSE = "extended-response";
  
  public ExtendedResponseHeaderProcessor() {}
  
  private HttpServletRequest request;
  private Response response;
  
  public static interface Handler {
    /**
     * @param headerValue not null
     * @return null to indicate unsupported headerValue
     */
    Object handle(String headerValue);
  }
  
  private Handler handler;

  /**
   * @param request the original request to take the header values from
   */
  public void setRequest(HttpServletRequest request) {
    this.request = request;
  }

  /**
   * @param response the original response to be extended
   */
  public void setResponse(Response response) {
    this.response = response;
  }

  /**
   * 
   * @param handler the handler to handle the header and create the extended response part
   */
  public void setHandler(Handler handler) {
    this.handler = handler;
  }
  
  private boolean responseExtended = false;

  /**
   * After the {@link #process()} invocation, indicates whether or not the actual response extension succeeded.
   * @return
   */
  public boolean responseExtended() {
    return responseExtended;
  }
  
  private Response extendedResponse; 
  
  /**
   * 
   * @return the extended response if and only if {@link #responseExtended()} returns {@code true};
   * otherwise {@code null}
   */
  public Response getExtendedResponse() {
    return extendedResponse;
  }
  
  /**
   * Perform response extension.
   * Before the invocation, {@link #setRequest(HttpServletRequest)}, {@link #setResponse(Response)}, {@link #setHandler(Handler)} must be called
   * After the invocation, {@link #responseExtended()} will indicate whether or not the actual response extension succeeded.
   */
  public void process() {
    if (request == null) {
      throw new IllegalStateException("the request has not been set");
    }
    if (response == null) {
      throw new IllegalStateException("the response has not been set");
    }
    if (handler == null) {
      throw new IllegalStateException("the handler has not been set");
    }
    
    final String header = request.getHeader(HEADER_NAME__EXTENDED_RESPONSE);
    if (header != null) {
      
      Map<String, Object> extendedResponseEntity = new HashMap<>();
      
      String[] headerValues = header.split("\\s*,\\s*");
      for (String headerValue: headerValues) {
        if (headerValue != null) {
        
          Object extendedEntityPart = handler.handle(headerValue);
          
          if (extendedEntityPart != null) {
            extendedResponseEntity.put(headerValue, extendedEntityPart);
            responseExtended = true;
          } else {
            // null means that the particular header value is not supported by the handler 
          }
        }
      }
      
      if (responseExtended) {
        Object basicResponseEntity = response.getEntity();
        
        Map<String, Object> newEntity = new HashMap<>();
        if (basicResponseEntity != null) {
          newEntity.put("basic-response", basicResponseEntity);
        }
        newEntity.put("extended-response", extendedResponseEntity);
        
        ResponseBuilder extendedResponseBuilder = Response.fromResponse(response);
        extendedResponseBuilder.entity(newEntity);
        
        extendedResponse = extendedResponseBuilder.build();
      }
    }
  }
}
