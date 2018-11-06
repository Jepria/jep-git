package org.jepria.server.service.rest.jaxrs.extendedresposne;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.glassfish.jersey.server.ContainerRequest;

public class ProducesExtendedResponseMethodInterceptor implements MethodInterceptor {

  @Override
  public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
    
    // TODO not [1], but compute the parameter index!
    ExtendedResponseProducerConfiguratorImpl configurator = 
        (ExtendedResponseProducerConfiguratorImpl)methodInvocation.getArguments()[1]; 
    
    final Object superResult = methodInvocation.proceed();
    
    /////
    final ContainerRequest request = configurator.getRequest();
    final ExtendedResponse.Factory factory = configurator.getFactory();
    
    if (request == null) {
      throw new IllegalStateException();
    }
    
    final List<String> values = parseRequestHeaderValues(request);
    if (values != null) {
      
      final Map<String, Object> extendedResponses = new HashMap<>();
      
      if (values != null && factory != null) {
        for (String value: values) {
          Object extendedResponse = factory.createExtendedResponse(value);
          if (extendedResponse != null) {
            extendedResponses.put(value, extendedResponse);
          }
        }
      }
      

      final Response superResponse = (Response)superResult;// TODO cast
      final Object body = superResponse.getEntity();
      
      Map<String, Object> newEntity = new HashMap<>();
      newEntity.put("basic-response", body != null ? body : Collections.emptyMap());
      newEntity.put("extended-response", extendedResponses);
      
      
      return Response.fromResponse(superResponse).entity(newEntity).build();
      
    } else {
      return superResult;
    }
  }
  
  private List<String> parseRequestHeaderValues(ContainerRequest request) {
    final String header = request.getHeaderString(ExtendedResponse.HEADER_NAME__EXTENDED_RESPONSE);
    
    if (header == null) {
      return null;
    }
    
    List<String> values = new ArrayList<>();
    String[] headerValues = header.split("\\s*,\\s*");
    for (String headerValue: headerValues) {
      if (headerValue != null) {
        values.add(headerValue);
      }
    }
    
    return values;
  }
}
