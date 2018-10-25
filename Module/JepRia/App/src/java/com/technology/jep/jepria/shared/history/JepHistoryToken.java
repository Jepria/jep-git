package com.technology.jep.jepria.shared.history;

import static com.technology.jep.jepria.shared.field.JepTypeEnum.BIGDECIMAL;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.BOOLEAN;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.DATE;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.DOUBLE;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.FLOAT;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.INTEGER;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.LIST_OF_OPTION;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.LIST_OF_PRIMITIVE;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.LIST_OF_RECORD;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.OPTION;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.STRING;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.TIME;
import static com.technology.jep.jepria.shared.history.JepHistoryConstant.LIST_VALUE_SEPARATOR;
import static com.technology.jep.jepria.shared.history.JepHistoryConstant.LIST_VALUE_SEPARATOR_REGEXP;
import static com.technology.jep.jepria.shared.history.JepHistoryConstant.MAP_NAME_TYPE_VALUE_SEPARATOR;
import static com.technology.jep.jepria.shared.history.JepHistoryConstant.MAP_PROPERTY_SEPARATOR;
import static com.technology.jep.jepria.shared.history.JepHistoryConstant.MAP_PROPERTY_SEPARATOR_REGEXP;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.time.JepTime;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Класс содержит методы преобразования объектов в их строковое представление (в History Token) и методы восстановления объектов из строкового
 * представления (из History Token'а).
 */
public class JepHistoryToken {

  /**
   * Преобразует объект в строковое представление (в History Token).<br/>
   * Особенности реализации и модификации метода:
   * <ul>
   *   <li>
   *     если в строковое представление преобразуется несколько свойств(полей) объекта, то разделитель между свойствами должен быть уникальным
   *     (смотри {@link com.technology.jep.jepria.shared.history.JepHistoryConstant});
   *   </li>
   *   <li>
   *     <u>НЕ должно быть преобразования (по умолчанию) НЕизвестного типа просто по <code>*.toString()</code></u>, т.к. восстановить такое значение 
   *     будет (в общем случае) невозможно - тип для нас неизвестен, значит для данного типа не реализованы методы восстановления из строкового 
   *     представления.
   *   </li>
   * </ul>
   * <br/>
   * Текущие поддерживаемые для преобразования в строковое представление типы:
   * <ul>
   *   <li><code>java.lang.String</code></li>
   *   <li><code>java.lang.Integer</code></li>
   *   <li><code>java.lang.Boolean</code></li>
   *   <li><code>java.util.Date</code></li>
   *   <li>{@link com.technology.jep.jepria.shared.time.JepTime}</li>
   *   <li>{@link com.technology.jep.jepria.shared.field.option.JepOption}</li>
   *   <li><code>java.util.List</code>&lt;{@link com.technology.jep.jepria.shared.field.option.JepOption}&gt;</li>
   * </ul>
   * <br/>
   * Для реализации преобразования объекта нового сложного типа данных в строковое представление, у данного сложного типа (класса) реализуется метод 
   * <code>toHistoryToken()</code>, который и вызывается в описываемом ({@link #valueToToken(Object value)}) методе.<br/>
   * 
   * @param value объект для преобразования в строковое представление
   * @return строковое представление объекта включая тип или <code>null</code>, если преобразовать объект в строку невозможно
   */
  public static String valueToToken(Object value) {
    String result = null;

    if(value == null) {
      result = null;
    } else if(value instanceof String) {
      result = STRING.toHistoryToken() + MAP_NAME_TYPE_VALUE_SEPARATOR + (String)value;
    } else if(value instanceof Float) {
      result = FLOAT.toHistoryToken() + MAP_NAME_TYPE_VALUE_SEPARATOR + value.toString();
    } else if(value instanceof BigDecimal) {
      result = BIGDECIMAL.toHistoryToken() + MAP_NAME_TYPE_VALUE_SEPARATOR + value.toString();
    } else if(value instanceof Integer) {
      result = INTEGER.toHistoryToken() + MAP_NAME_TYPE_VALUE_SEPARATOR + value.toString();
    } else if(value instanceof Double) {
      result = DOUBLE.toHistoryToken() + MAP_NAME_TYPE_VALUE_SEPARATOR + value.toString();
    } else if(value instanceof Boolean) {
      result = BOOLEAN.toHistoryToken() + MAP_NAME_TYPE_VALUE_SEPARATOR + (((Boolean)value) ? "1" : "0");
    } else if(value instanceof Date) {
      result = DATE.toHistoryToken() + MAP_NAME_TYPE_VALUE_SEPARATOR + ((Date)value).getTime();
    } else if(value instanceof JepTime) {
      result = TIME.toHistoryToken() + MAP_NAME_TYPE_VALUE_SEPARATOR + ((JepTime)value).toHistoryToken();
    } else if(value instanceof JepOption && !JepRiaUtil.isEmpty(value)) {
      result = OPTION.toHistoryToken() + MAP_NAME_TYPE_VALUE_SEPARATOR + ((JepOption)value).toHistoryToken();
    } else if(value instanceof List) {
      List<?> list = (List<?>)value;
      if (list.size() > 0) {
        Object item = list.get(0);
        if (item instanceof JepOption) {
          result = LIST_OF_OPTION.toHistoryToken() + MAP_NAME_TYPE_VALUE_SEPARATOR + JepOption.getListAsToken((List<JepOption>)list);
        } else if (item instanceof JepRecord) {
          result = LIST_OF_RECORD.toHistoryToken() + MAP_NAME_TYPE_VALUE_SEPARATOR + JepRecord.getListAsToken((List<JepRecord>)list);
        } else {
          StringBuilder resultToken = new StringBuilder();
          for (Object option: list) {
            if (option != null) {
              if (resultToken.length() > 0) {
                resultToken.append(LIST_VALUE_SEPARATOR);
              }
              resultToken.append(valueToToken(option));
            }
          }

          result = LIST_OF_PRIMITIVE.toHistoryToken() + MAP_NAME_TYPE_VALUE_SEPARATOR +  resultToken.toString();
        }
      }
    }

    return result;
  }

  /**
   * Преобразует строковое представление (History Token) в объект заданного типа.<br/>
   * Особенности реализации и модификации метода:
   * <ul>
   *   <li>
   *     <u>НЕ должно быть преобразования НЕизвестного типа просто в какой-то тип по умолчанию</u>, т.к. (в общем случае) дальнейшее использование
   *     полученного таким образом объекта будет некорректно и (с большой долей вероятности) будет приводить к исключениям (ошибкам).
   *   </li>
   * </ul>
   * <br/>
   * Текущие поддерживаемые для преобразования в объект типы:
   * <ul>
   *   <li>{@link com.technology.jep.jepria.shared.field.JepTypeEnum#STRING}</li>
   *   <li>{@link com.technology.jep.jepria.shared.field.JepTypeEnum#INTEGER}</li>
   *   <li>{@link com.technology.jep.jepria.shared.field.JepTypeEnum#BOOLEAN}</li>
   *   <li>{@link com.technology.jep.jepria.shared.field.JepTypeEnum#DATE}</li>
   *   <li>{@link com.technology.jep.jepria.shared.field.JepTypeEnum#TIME}</li>
   *   <li>{@link com.technology.jep.jepria.shared.field.JepTypeEnum#OPTION}</li>
   *   <li>{@link com.technology.jep.jepria.shared.field.JepTypeEnum#LIST_OF_OPTION}</li>
   * </ul>
   * <br/>
   * Для реализации преобразования строкового представления в объект нового сложного типа данных, у данного сложного типа (класса) реализуется 
   * конструктор или статический метод, которые по строковому представлению создают объект. Именно подобный конструктор или статический метод и 
   * вызывается в описываемом ({@link #tokenToValue(String token)}) методе для создания объекта заданного типа.<br/>
   * 
   * @param token строковое представление объекта включая тип
   * @return объект заданного типа созданный на основе строкового представления или <code>null</code>, если создать объект невозможно
   */
  public static Object tokenToValue(String token) {
    Object result = null;

    if(token == null) {
      return null;
    }

    String[] typeValue = token.split(MAP_NAME_TYPE_VALUE_SEPARATOR, 2); // Считаем, что значение - это все, что после первого разделителя.

    if(typeValue.length != 2) {
      return null;
    }

    String typeToken = typeValue[0];
    String valueToken = typeValue[1];

    Object type = JepTypeEnum.buildTypeFromToken(typeToken);

    if(type == null) {
      return null;
    }

    switch((JepTypeEnum)type) {
    case STRING:
      result = valueToken;
      break;
    case FLOAT:
      result = new Float(valueToken);
      break;
    case BIGDECIMAL:
      result = new BigDecimal(valueToken);
      break;
    case INTEGER:
      result = new Integer(valueToken);
      break;
    case DOUBLE:
      result = new Double(valueToken);
      break;
    case BOOLEAN:
      result = new Boolean("1".equals(valueToken));
      break;
    case DATE:
      long time = Long.parseLong(valueToken);
      result = new Date(time);
      break;
    case TIME:
      result = new JepTime(valueToken);
      break;
    case OPTION:
      result = new JepOption(valueToken);
      break;
    case LIST_OF_OPTION:
      result = JepOption.buildListFromToken(valueToken);
      break;
    case LIST_OF_PRIMITIVE:
      List<Object> resultList = new ArrayList<Object>();
      if(valueToken != null) {
        String[] listTokenTab = valueToken.split(LIST_VALUE_SEPARATOR_REGEXP);
        for(int i = 0; i < listTokenTab.length; i++) {
          if(listTokenTab[i] != null) {
            resultList.add(tokenToValue(listTokenTab[i]));
          }
        }
      }
      result = resultList;
      break;
    case LIST_OF_RECORD:
      result = JepRecord.buildListFromToken(valueToken);
      break;
    }
    
    return result;
  }

  /**
   * Преобразует набор содержащий пары имя объекта и сам объект в строковое представление (в так называемый History Token).
   *
   * @param map набор для преобразования в строковое представление (в так называемый History Token)
   * @return строковое представление набора (так называемый History Token)
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static String getMapAsToken(Map<String, Object> map) {
    StringBuilder resultToken = new StringBuilder();
    StringBuilder buffer = new StringBuilder();
    Set<Map.Entry<String, Object>> entries = map.entrySet();
    for(Map.Entry<String, Object> entry: entries) {
      String key = entry.getKey();
      Object value = entry.getValue();
      String valueToken = valueToToken(value);
      if(valueToken != null) {
        buffer.setLength(0);

        if(resultToken.length() > 0) {
          buffer.append(MAP_PROPERTY_SEPARATOR);
        }

        buffer.append(key);
        buffer.append(MAP_NAME_TYPE_VALUE_SEPARATOR);
        buffer.append(valueToken);

        resultToken.append(buffer.toString());
      }
    }

    return resultToken.toString();
  }

  /**
   * Преобразует строковое представление (так называемый History Token) в набор содержащий пары имя объекта и сам объект.
   *
   * @param mapToken строковое представление набора (так называемый History Token)
   * @return набор содержащий пары имя объекта и сам объект
   */
  public static Map<String, Object> buildMapFromToken(String mapToken) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    if(mapToken != null) {
      String[] mapTokenTab = mapToken.split(MAP_PROPERTY_SEPARATOR_REGEXP);
      int entryNumber = mapTokenTab.length;
      for(int i = 0; i < entryNumber; i++) {
        // Считаем, что тип и значение - это все, что после первого разделителя.
        String[] entry = mapTokenTab[i].split(MAP_NAME_TYPE_VALUE_SEPARATOR, 2);
        // Пытаемся восстановить объект, только если получили строго два элемента (предположительно тип и значение).
        if(entry.length == 2) {
          String nameToken = entry[0];
          String valueToken = entry[1];

          Object value = tokenToValue(valueToken);

          if(value != null) {
            resultMap.put(nameToken, value);
          }

        }
      }
    }
    return resultMap;
  }

}
