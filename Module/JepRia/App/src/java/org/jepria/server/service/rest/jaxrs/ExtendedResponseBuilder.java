package org.jepria.server.service.rest.jaxrs;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.Variant;

/**
 * {@link ResponseBuilder}, тело которого может быть расширено произвольным обектом.
 * Расширение производится методом {@link #extendedEntity(Object)}, при этом исходное тело помещается под ключ 'basic-entity',
 * а расширенное &mdash; под ключ 'extended-entity', например:
 * <br><br>
 * Тело ответа до расширения
 * <pre>
 * {
 *   "key": "value"
 * }
 * </pre>
 * Тело ответа после расширения командой <code>ExtendedResponseBuilder.extend(response).extendedEntity("{ 'extKey': 'extValue' }").build();</code>
 * <pre>
 * { 
 *   "basic-response": {
 *     "key": "value"
 *   },
 *   "extended-response": {
 *     "extKey": "extValue"
 *   }
 * }
 * </pre>
 * После расширения изменяется только тело ответа, все остальные свойства (статус, заголовки, и т.д.) остаются неизменными
 */
public class ExtendedResponseBuilder extends ResponseBuilder {

  private final ResponseBuilder originalResponse;

  private ExtendedResponseBuilder(Response originalResponse) {
    this.originalResponse = Response.fromResponse(originalResponse);

    // extract basic entity
    basicEntity = originalResponse.getEntity();
  }

  public static ExtendedResponseBuilder extend(ResponseBuilder originalResponse) {
    if (originalResponse == null) {
      throw new IllegalArgumentException("The original response must not be null");
    }
    return extend(originalResponse.build());
  }

  public static ExtendedResponseBuilder extend(Response originalResponse) {
    if (originalResponse == null) {
      throw new IllegalArgumentException("The original response must not be null");
    }
    return new ExtendedResponseBuilder(originalResponse);
  }



  private boolean responseExtended = false;

  private Object basicEntity = null;

  private Object extendedEntity = null;


  public ResponseBuilder extendedEntity(Object entity) {
    responseExtended = true;
    extendedEntity = entity;
    return this;
  }

  public ResponseBuilder basicEntity(Object entity) {
    responseExtended = true;
    basicEntity = entity;
    return this;
  }

  public Response build() {
    if (responseExtended) {
      
      Map<String, Object> compositeEntity = new HashMap<>();

      if (basicEntity != null) {
        compositeEntity.put("basic-response", basicEntity);
      }
      if (extendedEntity != null) {
        compositeEntity.put("extended-response", extendedEntity);
      }
      
      originalResponse.entity(compositeEntity);
    }      
    return originalResponse.build();
  }

  public ResponseBuilder clone() {
    // cloning not supported because returning a ResponseBuilder discards the extension 
    throw new UnsupportedOperationException(ExtendedResponseBuilder.class.getCanonicalName() + " does not suppot this method");
  }

  public ResponseBuilder entity(Object arg0, Annotation[] arg1) {
    responseExtended = false;
    return originalResponse.entity(arg0, arg1);
  }

  public ResponseBuilder entity(Object arg0) {
    responseExtended = false;
    return originalResponse.entity(arg0);
  }




  ///////// DELEGATE METHODS /////////

  public ResponseBuilder allow(Set<String> arg0) {
    return originalResponse.allow(arg0);
  }

  public ResponseBuilder allow(String... arg0) {
    return originalResponse.allow(arg0);
  }

  public ResponseBuilder cacheControl(CacheControl arg0) {
    return originalResponse.cacheControl(arg0);
  }

  public ResponseBuilder contentLocation(URI arg0) {
    return originalResponse.contentLocation(arg0);
  }

  public ResponseBuilder cookie(NewCookie... arg0) {
    return originalResponse.cookie(arg0);
  }

  public ResponseBuilder encoding(String arg0) {
    return originalResponse.encoding(arg0);
  }

  public ResponseBuilder expires(Date arg0) {
    return originalResponse.expires(arg0);
  }

  public ResponseBuilder header(String arg0, Object arg1) {
    return originalResponse.header(arg0, arg1);
  }

  public ResponseBuilder language(Locale arg0) {
    return originalResponse.language(arg0);
  }

  public ResponseBuilder language(String arg0) {
    return originalResponse.language(arg0);
  }

  public ResponseBuilder lastModified(Date arg0) {
    return originalResponse.lastModified(arg0);
  }

  public ResponseBuilder link(String arg0, String arg1) {
    return originalResponse.link(arg0, arg1);
  }

  public ResponseBuilder link(URI arg0, String arg1) {
    return originalResponse.link(arg0, arg1);
  }

  public ResponseBuilder links(Link... arg0) {
    return originalResponse.links(arg0);
  }

  public ResponseBuilder location(URI arg0) {
    return originalResponse.location(arg0);
  }

  public ResponseBuilder replaceAll(MultivaluedMap<String, Object> arg0) {
    return originalResponse.replaceAll(arg0);
  }

  public ResponseBuilder status(int arg0, String arg1) {
    return originalResponse.status(arg0, arg1);
  }

  public ResponseBuilder status(int arg0) {
    return originalResponse.status(arg0);
  }

  public ResponseBuilder status(Status status) {
    return originalResponse.status(status);
  }

  public ResponseBuilder status(StatusType status) {
    return originalResponse.status(status);
  }

  public ResponseBuilder tag(EntityTag arg0) {
    return originalResponse.tag(arg0);
  }

  public ResponseBuilder tag(String arg0) {
    return originalResponse.tag(arg0);
  }

  public ResponseBuilder type(MediaType arg0) {
    return originalResponse.type(arg0);
  }

  public ResponseBuilder type(String arg0) {
    return originalResponse.type(arg0);
  }

  public ResponseBuilder variant(Variant arg0) {
    return originalResponse.variant(arg0);
  }

  public ResponseBuilder variants(List<Variant> arg0) {
    return originalResponse.variants(arg0);
  }

  public ResponseBuilder variants(Variant... arg0) {
    return originalResponse.variants(arg0);
  }

  ////////////////////////////////////


}
