package org.jepria.server.data;

import org.jepria.server.service.rest.gson.JsonBindingProvider;

import javax.json.bind.Jsonb;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Утилитарные методы для работы с Dto
 */
// TODO класс-помойка!
public class DtoUtil {
  
  private DtoUtil() {}
  
  /**
   * Преобразование Dto-объекта в Map
   * @param dto
   * @return null for null
   */
  public static Map<String, Object> dtoToMap(Object dto) {
    if (dto == null) {
      return null;
    }

    final Type type = new HashMap<String, Object>().getClass();
    final Jsonb jsonb = JsonBindingProvider.getJsonb();
    final Map<String, Object> map = jsonb.fromJson(jsonb.toJson(dto), type);
    return map;
  }
//
//  /**
//   * Преобразование Map в Dto-объект
//   * @param map
//   * @param dtoClass
//   * @return null for null
//   */
//  public static <T> T mapToDto(Map<String, ?> map, Class<T> dtoClass) {
//    if (map == null) {
//      return null;
//    }
//
//    final Jsonb jsonb = JsonbBuilder.create();
//    final T resource = jsonb.fromJson(jsonb.toJson(map), dtoClass);
//    return resource;
//  }
  
  /**
   * Преобразование Map в {@link OptionDto}-объект
   * @param entityDto
   * @return null for null
   */
  public static OptionDto mapToOptionDto(Object entityDto) {
    if (entityDto == null) {
      return null;
    }

    // TODO
    if (true) throw new UnsupportedOperationException();

    // TODO get key and value fields from entityDto and RecordDefinition using reflection
    OptionDto optionDto = new OptionDto();
    optionDto.setName("name-of-" + entityDto);
    optionDto.setValue("value-of-" + entityDto);
    return optionDto;
  }
}
