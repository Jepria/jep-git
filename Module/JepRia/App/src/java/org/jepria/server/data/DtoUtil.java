package org.jepria.server.data;

import org.jepria.server.service.rest.gson.JsonBindingProvider;

import javax.json.bind.Jsonb;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Утилитарные методы для работы с Dto
 */
public final class DtoUtil {
  
  private DtoUtil() {}

  /**
   * Преобразование Dto-объекта в Map
   * @param dto
   * @return null for null
   */
  // TODO remove this method?
  public static Map<String, Object> dtoToMap(Object dto) {
    if (dto == null) {
      return null;
    }

    final Type type = new HashMap<String, Object>().getClass();
    final Jsonb jsonb = JsonBindingProvider.getJsonb();
    final Map<String, Object> map = jsonb.fromJson(jsonb.toJson(dto), type);
    return map;
  }





  public static String like(String what) {
    return contains(what);
  }
  public static String contains(String what) {
    return what == null ? null : ("%" + what + "%");
  }
  public static String startsWith(String what) {
    return what == null ? null : (what + "%");
  }
  public static String endsWith(String what) {
    return what == null ? null : ("%" + what);
  }
  public static String convertList(List<?> list) {
    return convertList(list, ";");
  }
  public static String convertList(List<?> list, String delimiter) {
    if (list == null) {
      return null;
    }

    if (delimiter == null) {
      delimiter = ";";
    }

    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (Object o: list) {
      if (first) {
        first = false;
      } else {
        sb.append(delimiter);
      }
      String ostr = o == null ? "" : String.valueOf(o);
      sb.append(ostr);
    }

    return sb.toString();
  }
}
