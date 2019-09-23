package org.jepria.server.service.rest.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.container.ResourceInfo;
import java.util.function.Supplier;

/**
 * Builds {@link Gson} instances from various configuration annotations (e.g. {@link JsonConfig} or {@link JsonConfigPrepared})
 */
public class AnnotatedGsonBuilder {

  /**
   * may be {@code null}
   */
  protected final ResourceInfo resourceInfo;

  public AnnotatedGsonBuilder() {
    this(null);
  }
  
  /**
   * @param resourceInfo may be {@code null}
   */
  public AnnotatedGsonBuilder(ResourceInfo resourceInfo) {
    this.resourceInfo = resourceInfo;
  }

  public Gson build() {

    JsonConfigPrepared jsonConfigGson = null;
    JsonConfig jsonConfigParams = null;

    
    if (resourceInfo != null) {
      // prior resource method annotations over resource class annotations
      jsonConfigGson = resourceInfo.getResourceMethod().getAnnotation(JsonConfigPrepared.class);
      jsonConfigParams = resourceInfo.getResourceMethod().getAnnotation(JsonConfig.class);
  
      // fail-fast on both annotations present
      if (jsonConfigGson != null && jsonConfigParams != null) {
        throw new IllegalArgumentException("The resource method " + getCanonicalResourceMethodName() 
        + " can be annotated with either " + JsonConfigPrepared.class.getCanonicalName() 
        + " or " + JsonConfig.class.getCanonicalName() + ", but not both.");
      }
      if (jsonConfigGson == null && jsonConfigParams == null) {
        jsonConfigGson = resourceInfo.getResourceClass().getAnnotation(JsonConfigPrepared.class);
        jsonConfigParams = resourceInfo.getResourceClass().getAnnotation(JsonConfig.class);
  
        // fail-fast on both annotations present
        if (jsonConfigGson != null && jsonConfigParams != null) {
          throw new IllegalArgumentException("The resource " + resourceInfo.getResourceClass().getCanonicalName() 
              + " can be annotated with either " + JsonConfigPrepared.class.getCanonicalName() 
              + " or " + JsonConfig.class.getCanonicalName() + ", but not both.");
        }
      }
    }
    

    if (jsonConfigGson == null && jsonConfigParams == null) {
      // no json config
      return new DefaultGsonBuilder().get().create();

    } else if (jsonConfigGson != null && jsonConfigParams == null) {
      // prepared Gson
      return fromConfigGson(jsonConfigGson);

    } else if (jsonConfigGson == null && jsonConfigParams != null) {
      // build Gson from params
      return fromConfigParams(jsonConfigParams);

    } else {
      throw new IllegalStateException();
    }
  }

  /**
   * Asserts that {@link #resourceInfo} is not {@code null} 
   */
  protected String getCanonicalResourceMethodName() {
    String methodName = resourceInfo.getResourceMethod().getName();
    String methodNameCanonical = resourceInfo.getResourceMethod().toString();
    String methodNameInClass = methodNameCanonical.substring(methodNameCanonical.indexOf(methodName), methodNameCanonical.length());
    return resourceInfo.getResourceClass().getCanonicalName() + "." + methodNameInClass;
  }

  protected Gson fromConfigGson(JsonConfigPrepared config) {
    final Class<? extends Supplier<Gson>> gsonSupplierClass = config.value();

    final Supplier<Gson> gsonSupplier;
    try {
      gsonSupplier = gsonSupplierClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    return gsonSupplier.get();
  }

  protected Gson fromConfigParams(JsonConfig config) {
    GsonBuilder builder = new DefaultGsonBuilder().get();

    if (config.generateNonExecutableJson()) {
      builder.generateNonExecutableJson();
    }
    if (config.serializeNulls()) {
      builder.serializeNulls();
    }
    if (config.prettyPrinting()) {
      builder.setPrettyPrinting();
    }
    if (!config.escapeHtml()) {
      builder.disableHtmlEscaping();
    }
    builder.setDateFormat(config.dateFormat());

    return builder.create();
  }
}
