package org.jepria.server.service.rest.gson;

import com.google.gson.Gson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

/**
 * Configures HTTP request/response serialization using {@link Gson}
 * by supplying a prepared {@link Gson} instance.
 * <br/>
 * Example usage:
 * <pre>
 *
 * public class ResourceJaxrsAdapter {
 *
 *   // Пример сложной настройки формата json-ответа метода с помощью аннотации &#064;JsonConfigPrepared
 *   // и отдельного класса с конфигурацией Gson
 *   &#064;GET
 *   &#064;JsonConfigPrepared(JsonConfigComplex.class)
 *   public Object jsonConfigComplex() {
 *     Object response = createResponse();
 *     return response;
 *   }
 *
 *   public static class JsonConfigComplex implements Supplier<Gson> {
 *     &#064;Override
 *     public Gson get() {
 *       GsonBuilder gsonBuilder = new GsonBuilder();
 *       complexSetup(gsonBuilder); // сколь угодно сложная настройка
 *       return gsonBuilder.create();
 *     }
 *   }
 * }
 * </pre>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonConfigPrepared {
  Class<? extends Supplier<Gson>> value();
}
