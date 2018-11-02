package org.jepria.server.service.rest.jaxrs;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Класс для создания расширенных ответов на основе заголовка запроса и обработчика
 */
public class ExtendedResponseHeaderProcessor {
  
  public static final String HEADER_NAME__EXTENDED_RESPONSE = "extended-response";
  
  /**
   * @param request the request to take 'extended-response' header values from
   */
  public ExtendedResponseHeaderProcessor(HttpServletRequest request, Handler handler) {
    this.request = request;
    this.handler = handler;
  }
  
  private final HttpServletRequest request;
  
  public static interface Handler {
    /**
     * @param headerValue not null
     * @return null to indicate unsupported headerValue
     */
    Object handle(String headerValue);
  }
  
  private final Handler handler;

  private Map<String, Object> extendedResponses; 
  
  /**
   * @return the extended responses mapped to each 'extended-response' header value; non-empty or null
   */
  public Map<String, Object> getExtendedResponses() {
    return extendedResponses;
  }
  
  /**
   * Perform response extension.
   */
  public void process() {
    if (request != null && handler != null) {
      final String header = request.getHeader(HEADER_NAME__EXTENDED_RESPONSE);
      if (header != null) {
        
        Map<String, Object> extendedResponses = new HashMap<>();
        
        String[] headerValues = header.split("\\s*,\\s*");
        for (String headerValue: headerValues) {
          if (headerValue != null) {
          
            Object extendedEntityPart = handler.handle(headerValue);
            
            if (extendedEntityPart != null) {
              extendedResponses.put(headerValue, extendedEntityPart);
            } else {
              // null means that the particular header value is not supported by the handler 
            }
          }
        }
        
        if (!extendedResponses.isEmpty()) {
          this.extendedResponses = extendedResponses;
        }
      }
    }
  }
}
