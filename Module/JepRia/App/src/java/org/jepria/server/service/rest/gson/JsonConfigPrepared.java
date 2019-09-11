package org.jepria.server.service.rest.gson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

import com.google.gson.Gson;

/**
 * Configures HTTP request/response serialization using {@link Gson}
 * by supplying a prepared {@link Gson} instance.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonConfigPrepared {
  Class<? extends Supplier<Gson>> value();
}
