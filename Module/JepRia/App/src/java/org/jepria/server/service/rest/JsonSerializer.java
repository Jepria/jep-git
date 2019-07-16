package org.jepria.server.service.rest;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class JsonSerializer {
  /**
   * @param json may be {@code null}
   * @return {@code null} for {@code null} 
   * @throws JsonParseException if deserialization fails
   */
  public Map<String, ?> deserialize(String json) throws JsonParseException {
    if (json == null) {
      return null;
    }
    
    final Gson gson = new Gson();
    
    try {
      return gson.fromJson(json, new TypeToken<Map<String, ?>>() { }.getType());
      
    } catch (IllegalStateException | JsonSyntaxException e) {
      // Note: do not catch Throwable, catch particular common exceptions instead
      throw new JsonParseException(e);
    }
  }
  
  /**
   * @param reader may be {@code null}
   * @return {@code null} for {@code null} 
   * @throws JsonParseException if deserialization fails
   */
  public Map<String, ?> deserialize(Reader reader) throws JsonParseException {
    if (reader == null) {
      return null;
    }
    
    final Gson gson = new Gson();
    
    try (Reader r = reader) {
      return gson.fromJson(reader, new TypeToken<Map<String, ?>>() { }.getType());
      
    } catch (IllegalStateException | JsonSyntaxException e) {
      // Note: do not catch Throwable, catch particular common exceptions instead
      throw new JsonParseException(e);
      
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * 
   * @param object may be {@code null}
   * @param out to write result to
   */
  public void serialize(Object object, Appendable out) {
    if (object == null) {
      return;
    }
    new Gson().toJson(object, out);
  }
}
