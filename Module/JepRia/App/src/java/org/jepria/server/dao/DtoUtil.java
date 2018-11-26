package org.jepria.server.dao;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

/**
 * Утилитарные методы для работы с Dto
 */
// TODO класс-помойка!
public class DtoUtil {
  
  private DtoUtil() {}
  
  public static Map<String, ?> dtoToMap(Object dto) {
    final Type type = new HashMap<String, Object>().getClass().getGenericSuperclass();
    Jsonb jsonb = JsonbBuilder.create();
    final Map<String, ?> map = jsonb.fromJson(jsonb.toJson(dto), type);
    return map;
  }
  
  public static <T> T mapToDto(Map<String, ?> map, Class<T> dtoClass) {
    final Type type = dtoClass.getGenericSuperclass();
    Jsonb jsonb = JsonbBuilder.create();
    T resource = jsonb.fromJson(jsonb.toJson(map), type);
    return resource;
  }
  
}
