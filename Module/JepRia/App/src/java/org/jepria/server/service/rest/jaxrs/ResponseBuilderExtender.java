package org.jepria.server.service.rest.jaxrs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

/**
 * Class for extending ResponseBuilders (namely, while processing 'extended-response' request headers)
 */
public final class ResponseBuilderExtender {
  
  public static final String HEADER_NAME__EXTENDED_RESPONSE = "extended-response";
  
  private final ResponseBuilder originalResponseBuilder;
  
  private final ExtendedResponseHandler handler;
  
  private ResponseBuilderExtender(ResponseBuilder originalResponseBuilder, ExtendedResponseHandler handler) {
    this.originalResponseBuilder = originalResponseBuilder;
    this.handler = handler;
  }
  
  /**
   * Extends the ResponseBuilder with the handler
   * @param responseBuilder the {@link ResponseBuilder} to extend, not null
   * @param handler the handler to extend the underlying {@link ResponseBuilder} with, not null
   * @return a ResponseBuilderExtender instance to apply the handler 
   * @throws IllegalArgumentException if either responseBuilder or handler is null
   */
  public static ResponseBuilderExtender extendWithHandler(ResponseBuilder responseBuilder, ExtendedResponseHandler handler) {
    if (responseBuilder == null) {
      throw new IllegalArgumentException("responseBuilder must not be null");
    }
    if (handler == null) {
      throw new IllegalArgumentException("handler must not be null");
    }
    
    return new ResponseBuilderExtender(responseBuilder, handler);
  }
  
  /**
   * Extends the underlying {@link ResponseBuilder} by applying the handler to the particular extendedResponseValue
   * @param extendedResponseValue
   * @return the extended ResponseBuilder
   */
  public ResponseBuilder appliedToValue(String extendedResponseValue) {
    final Object extendedResponse;
    try {
      extendedResponse = handler.handle(extendedResponseValue);
    } catch (UnsupportedHeaderValueException e) {
      final String reasonPhrase = "The value '" + e.unsupportedHeaderValue + "' for the header '" + HEADER_NAME__EXTENDED_RESPONSE + "' is unsupported by the request";
      return Response.status(Status.BAD_REQUEST.getStatusCode(), reasonPhrase);
    }

    ResponseBuilder response = originalResponseBuilder;
    
    // move current entity under 'original-response' key, add 'extended-response' field
    Response rOriginalResponse = originalResponseBuilder.build();
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
   * Extends the underlying {@link ResponseBuilder} by applying the handler to the value of {@link #HEADER_NAME__EXTENDED_RESPONSE} request header
   * @param request not null
   * @throws IllegalArgumentException if request is null
   * @see 
   */
  public ResponseBuilder appliedToRequest(HttpServletRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("request must not be null");
    }
    
    if (Collections.list(request.getHeaderNames()).contains(HEADER_NAME__EXTENDED_RESPONSE)) {
      String extendedResponseValue = request.getHeader(HEADER_NAME__EXTENDED_RESPONSE);
      return appliedToValue(extendedResponseValue);
    }
    
    return originalResponseBuilder;
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
     * Handles particular extendedResponseValue within a request to the endpoint 
     * @param extendedResponseValue
     * @return object ready to be serialized into a response
     * @throws UnsupportedHeaderValueException if the implementor cannot handle such extendedResponseValue  
     */
    Object handle(String extendedResponseValue) throws UnsupportedHeaderValueException;
  }
}
