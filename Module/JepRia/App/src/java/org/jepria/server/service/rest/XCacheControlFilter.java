package org.jepria.server.service.rest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.List;

/**
 * The filter overrides the {@code Cache-Control} header with the {@code X-Cache-Control} header, if any
 */
public class XCacheControlFilter implements ContainerRequestFilter {
  @Override
  public void filter(ContainerRequestContext containerRequestContext) throws IOException {
    MultivaluedMap<String, String> headers = containerRequestContext.getHeaders();
    List<String> xCacheControlValue = headers.get("X-Cache-Control");
    if (xCacheControlValue != null && xCacheControlValue.size() > 0 && xCacheControlValue.iterator().next().length() > 0) {
      headers.put("Cache-Control", xCacheControlValue);
    }
  }
}
