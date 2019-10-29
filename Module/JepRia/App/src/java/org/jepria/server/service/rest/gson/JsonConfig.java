package org.jepria.server.service.rest.gson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configures HTTP request/response serialization using {@link com.google.gson.Gson}
 * by specifying some parameters of {@link com.google.gson.GsonBuilder}.
 * <br/>
 * Example usage:
 * <pre>
 *
 * public class ResourceJaxrsAdapter {
 *   // Пример простой настройки формата json-ответа метода с помощью аннотации &#064;JsonConfig
 *   &#064;GET
 *   &#064;JsonConfig(serializeNulls = true, escapeHtml = false, prettyPrinting = true, dateFormat = "yyyy/MM/dd")
 *   public Object jsonConfigSimple() {
 *     Object response = createResponse();
 *     return response;
 *   }
 * }
 * </pre>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonConfig {
  /**
   * @see com.google.gson.GsonBuilder#generateNonExecutableJson()
   */
  boolean generateNonExecutableJson()   default false;  // default is com.google.gson.Gson.DEFAULT_JSON_NON_EXECUTABLE

  /**
   * @see com.google.gson.GsonBuilder#serializeNulls()
   */
  boolean serializeNulls()              default false;  // default is com.google.gson.Gson.DEFAULT_SERIALIZE_NULLS

  /**
   * @see com.google.gson.GsonBuilder#setPrettyPrinting()
   */
  boolean prettyPrinting()              default false;  // default is com.google.gson.Gson.DEFAULT_PRETTY_PRINT

  /**
   * @see com.google.gson.GsonBuilder#disableHtmlEscaping()
   */
  boolean escapeHtml()                  default true;   // default is com.google.gson.Gson.DEFAULT_ESCAPE_HTML

  /**
   * @see com.google.gson.GsonBuilder#setDateFormat(String)
   */
  String dateFormat()                   default DefaultGsonBuilder.DEFAULT_DATE_FORMAT;
}
