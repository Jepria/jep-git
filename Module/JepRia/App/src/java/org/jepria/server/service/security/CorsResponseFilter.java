package org.jepria.server.service.security;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

/**
 * <pre>
 * Обработка CORS.
 * Добавляет заголовки в ответ.
 * Дает полный доступ к API с других доменов.
 * </pre>
 */
public class CorsResponseFilter implements ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    responseContext.getHeaders().add(
      "Access-Control-Allow-Origin", "*");
    responseContext.getHeaders().add(
      "Access-Control-Allow-Credentials", "true");
    responseContext.getHeaders().add(
      "Access-Control-Allow-Headers",
      "Origin, Content-Type, Accept, Authorization, Extended-Response, X-Cache-Control, Cache-Control");
    responseContext.getHeaders().add(
      "Access-Control-Allow-Methods",
      "GET, POST, PUT, DELETE, OPTIONS, HEAD");
  }
}
