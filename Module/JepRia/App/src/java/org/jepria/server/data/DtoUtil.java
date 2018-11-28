package org.jepria.server.data;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.jepria.compat.CoreCompat;

/**
 * Утилитарные методы для работы с Dto
 */
// TODO класс-помойка!
public class DtoUtil {
  
  private DtoUtil() {}
  
  /**
   * Преобразование Dto-объекта в Map 
   * @param dto
   * @return
   */
  public static Map<String, ?> dtoToMap(Object dto) {
    final Type type = new HashMap<String, Object>().getClass();
    final Jsonb jsonb = JsonbBuilder.create();
    final Map<String, ?> map = jsonb.fromJson(jsonb.toJson(dto), type);
    return map;
  }
  
  /**
   * Преобразование Map в Dto-объект
   * @param map
   * @param dtoClass
   * @return
   */
  public static <T> T mapToDto(Map<String, ?> map, Class<T> dtoClass) {
    final Jsonb jsonb = JsonbBuilder.create();
    final T resource = jsonb.fromJson(jsonb.toJson(map), dtoClass);
    return resource;
  }
  
  /**
   * Преобразование Map в {@link OptionDto}-объект
   * @param map
   * @return
   */
  public static OptionDto mapToOptionDto(Map<String, ?> map) {
    final OptionDto dto = new OptionDto();
    dto.setName((String)map.get(CoreCompat.OPTION_NAME_KEY));
    Object value = map.get(CoreCompat.OPTION_VALUE_KEY);
    String valueStr = value == null ? null : value.toString();
    dto.setValue(valueStr);
    return dto;
  }
  
}