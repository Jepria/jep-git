package org.jepria.server.service.rest.gson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configures HTTP request/response serialization using {@link com.google.gson.Gson}
 * by specifying parameters of {@link com.google.gson.GsonBuilder}.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonConfig {
  boolean generateNonExecutableJson()   default false;  // default is com.google.gson.Gson.DEFAULT_JSON_NON_EXECUTABLE
  boolean serializeNulls()              default false;  // default is com.google.gson.Gson.DEFAULT_SERIALIZE_NULLS
  boolean prettyPrinting()              default false;  // default is com.google.gson.Gson.DEFAULT_PRETTY_PRINT 
  boolean escapeHtml()                  default true;   // default is com.google.gson.Gson.DEFAULT_ESCAPE_HTML
  String dateFormat()                   default "dd.MM.yyyy";
}
