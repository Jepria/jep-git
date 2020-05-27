package org.jepria.server.service.rest.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.function.Supplier;

public class DefaultGsonBuilder implements Supplier<GsonBuilder> {
  @Override
  public GsonBuilder get() {
    return new GsonBuilder().setPrettyPrinting().setDateFormat(DEFAULT_DATE_FORMAT);
  }
  
  public Gson build() {
    return get().create();
  }

  public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd"; // iso 8601
}
