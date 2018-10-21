package org.jepria.server.service.rest.jaxrs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

/**
 * Class for extending responses for 'extend-response' request headers
 */
public class ResponseExtender {
  
  private ResponseExtender() {}
  
  /**
   * Extends the response using the handler applied to each headerValue from the set
   * @param originalResponse
   * @param handler
   * @param headerValues
   * @return
   */
  public static ResponseBuilder extend(ResponseBuilder originalResponse, ExtendResponseHandler handler, Set<String> headerValues) {
    if (originalResponse == null) {
      originalResponse = Response.ok();
    }
    
    if (headerValues == null || headerValues.size() == 0) {
      return originalResponse;
      
    } else {
      
      final Map<String, Object> extendedResponseItems = new HashMap<>();
      
      for (String extendResponseValue: headerValues) {
        try {
          Object extendedResponse = handler.handle(extendResponseValue);
          if (extendedResponse != null) {
            extendedResponseItems.put(extendResponseValue, extendedResponse);
          }
        } catch (UnsupportedHeaderValueException e) {
          final String reasonPhrase = "The value '" + e.unsupportedHeaderValue + "' for the header '" + HEADER_NAME__EXTEND_RESPONSE + "' is unsupported by the request";
          return Response.status(Status.BAD_REQUEST.getStatusCode(), reasonPhrase);
        }
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
      newEntity.put("extended-response", extendedResponseItems);
      response.entity(newEntity);
      
      return response;
    }
  }
  
  /**
   * Extends the response using the handler applied to each headerValue from the {@link #HEADER_NAME__EXTEND_RESPONSE} header of the request
   * @param originalResponse
   * @param handler
   * @param headerValues
   * @return
   */
  public static ResponseBuilder extend(ResponseBuilder originalResponse, ExtendResponseHandler handler, HttpServletRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("request must not be null");
    }
    
    String headerValue = request.getHeader(HEADER_NAME__EXTEND_RESPONSE);
    if (headerValue == null) {
      return originalResponse;
      
    } else {
      Set<String> headerValues = parseCommaSeparatedHeaderValue(headerValue);
      return extend(originalResponse, handler, headerValues);
    }
  }

  public static final String HEADER_NAME__EXTEND_RESPONSE = "extend-response";

  public static class UnsupportedHeaderValueException extends Exception {
    private static final long serialVersionUID = 1L;
    
    private final String unsupportedHeaderValue;

    public UnsupportedHeaderValueException(String unsupportedHeaderValue) {
      this.unsupportedHeaderValue = unsupportedHeaderValue;
    }
  }
  
  public static interface ExtendResponseHandler {
    /**
     * Handle particular extendResponseValue within a request to the endpoint (that value is one of comma-separated values of a 
     * {@link StandardResourceEndpoint#HEADER_NAME__EXTEND_RESPONSE} request header, if any) 
     * @param extendResponseValue
     * @return object ready to be serialized into a response
     * @throws UnsupportedHeaderValueException if the implementor cannot handle such extendResponseValue  
     */
    Object handle(String extendResponseValue) throws UnsupportedHeaderValueException;
  }
  
  /**
   * @param commaSeparatedHeaderValue
   * @return or empty set, never null
   */
  private static Set<String> parseCommaSeparatedHeaderValue(String commaSeparatedHeaderValue) {
    final Set<String> ret = new HashSet<>();

    if (commaSeparatedHeaderValue != null) {
      String[] headerValues = commaSeparatedHeaderValue.split("\\s*,\\s*");
      for (String headerValue: headerValues) {
        if (headerValue != null && headerValue.length() > 0) {
          ret.add(headerValue);
        }
      }
    }

    return ret;
  }
}
