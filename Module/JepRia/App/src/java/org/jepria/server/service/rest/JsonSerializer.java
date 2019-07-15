package org.jepria.server.service.rest;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonSerializer {
  /**
   * 
   * @param json may be {@code null}
   * @return
   * @throws RuntimeException if deserialization fails
   */
  public Map<String, Object> deserialize(String json) {
    if (json == null) {
      return null;
    }
    return new Gson().fromJson(json, new TypeToken<Map<String, Object>>() { }.getType());
  }
  
  /**
   * 
   * @param object may be {@code null}
   * @param out to write result to
   * @throws RuntimeException if serialization fails
   */
  public void serialize(Object object, Appendable out) {
    if (object == null) {
      return;
    }
    new Gson().toJson(object, out);
  }
}
