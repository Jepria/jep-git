package org.jepria.server.service.rest.jaxrs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

/**
 * Class for extending responses for 'extended-response' request headers
 */
public class ExtendedResponse {
  
  public static final String HEADER_NAME__EXTENDED_RESPONSE = "extended-response";
  
  private ExtendedResponse() {}
  
  /**
   * Extends the response using the handler applied to the extendedResponseValue
   * @param originalResponse not null
   * @param handler
   * @param extendedResponseValue not null
   * @return
   */
  public static ResponseBuilder from(ResponseBuilder originalResponse, ExtendedResponseHandler handler, String extendedResponseValue) {
    if (originalResponse == null) {
      throw new IllegalArgumentException("extendedResponseValue must not be null");
    }
    
    if (extendedResponseValue == null) {
      throw new IllegalArgumentException("extendedResponseValue must not be null");
    }
    
    final Object extendedResponse;
    try {
      extendedResponse = handler.handle(extendedResponseValue);
    } catch (UnsupportedHeaderValueException e) {
      final String reasonPhrase = "The value '" + e.unsupportedHeaderValue + "' for the header '" + HEADER_NAME__EXTENDED_RESPONSE + "' is unsupported by the request";
      return Response.status(Status.BAD_REQUEST.getStatusCode(), reasonPhrase);
    }

    ResponseBuilder response = originalResponse;
    
    // move current entity under 'original-response' key, add 'extended-response' field
    Response rOriginalResponse = originalResponse.build();
    Object originalEntity = rOriginalResponse.getEntity();
    response = Response.fromResponse(rOriginalResponse);

    Map<String, Object> newEntity = new HashMap<>();
    if (originalEntity != null) {
      newEntity.put("original-response", originalEntity);
    }
    // extended-response map is always present in the main response, even if it is empty
    newEntity.put("extended-response", extendedResponse);
    response.entity(newEntity);
    
    return response;
  }
  
  /**
   * Extends the response using the handler applied to the value of {@link #HEADER_NAME__EXTENDED_RESPONSE} request header
   * @param originalResponse
   * @param handler
   * @param request not null
   * @return
   */
  public static ResponseBuilder from(ResponseBuilder originalResponse, ExtendedResponseHandler handler, HttpServletRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("request must not be null");
    }
    
    if (Collections.list(request.getHeaderNames()).contains(HEADER_NAME__EXTENDED_RESPONSE)) {
      String extendedResponseValue = request.getHeader(HEADER_NAME__EXTENDED_RESPONSE);
      if (extendedResponseValue != null) {
        return from(originalResponse, handler, extendedResponseValue);
      }
    }
    
    return originalResponse;
  }

  public static class UnsupportedHeaderValueException extends Exception {
    private static final long serialVersionUID = 1L;
    
    private final String unsupportedHeaderValue;

    public UnsupportedHeaderValueException(String unsupportedHeaderValue) {
      this.unsupportedHeaderValue = unsupportedHeaderValue;
    }
  }
  
  public static interface ExtendedResponseHandler {
    /**
     * Handle particular extendedResponseValue within a request to the endpoint (that value is one of comma-separated values of a 
     * {@link StandardResourceEndpoint#HEADER_NAME__EXTENDED_RESPONSE} request header, if any) 
     * @param extendedResponseValue
     * @return object ready to be serialized into a response
     * @throws UnsupportedHeaderValueException if the implementor cannot handle such extendedResponseValue  
     */
    Object handle(String extendedResponseValue) throws UnsupportedHeaderValueException;
  }
}
