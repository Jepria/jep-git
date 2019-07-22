package org.jepria.server.service.rest.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DefaultGsonBuilder {
  public Gson build() {
    return new GsonBuilder().setPrettyPrinting().create();
  }
}
