package org.jepria.server.service.rest.gson;

import java.util.function.Supplier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DefaultGsonBuilder implements Supplier<GsonBuilder> {
  @Override
  public GsonBuilder get() {
    return new GsonBuilder().setPrettyPrinting();
  }
  
  public Gson build() {
    return get().create();
  }
}
