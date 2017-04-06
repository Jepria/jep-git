package com.technology.jep.jepria.shared.util;

import static com.technology.jep.jepria.shared.JepRiaConstant.UNDEFINED_INT;

import java.util.List;

import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.lob.JepClob;

public class JepRiaUtil {

  /**
   * Функция определяет: является ли переданный объект пустым.
   * 
   * @param obj проверяемый объект
   */
  public static boolean isEmpty(Object obj) {
    if(obj == null) {
      return true;
    } else if(obj instanceof String) {
      String objStr = (String)obj;
      return (objStr.trim().length() == 0) ? true : false;
    } else if(obj instanceof Integer) {
      Integer objInteger = (Integer)obj;
      return (objInteger.intValue() == UNDEFINED_INT) ? true : false;
    } else if (obj instanceof List){
      List<?> objList = (List<?>)obj;
      return objList.isEmpty();
    } else if (obj instanceof JepOption) {
      JepOption objJepOption = (JepOption)obj;
      return (objJepOption.equals(JepOption.EMPTY_OPTION)) ? true : false;
    } else if (obj instanceof JepClob) {
      JepClob objClob = (JepClob)obj;
      return (JepRiaUtil.isEmpty(objClob.getBigText())) ? true : false;
    }
    return false;
  }

  /**
   * Сравнение объектов на равенство.
   * 
   * @param obj1      первый сравниваемый объект
   * @param obj2      второй сравниваемый объект
   * @return признак равенства сравниваемых объектов
   */
  public static boolean equalWithNull(Object obj1, Object obj2) {
    if (obj1 == obj2) {
      return true;
    } else if (obj1 == null) {
      return false;
    } else {
      return obj1.equals(obj2);
    }
  }
  
  /**
   * Функция определяет: может ли переданная ей строка быть преобразована 
   * к типу Integer.
   * 
   * @param value строка, которую проверяем
   * 
   * @return возвращает true, если переданная строка может быть преобразована к типу Integer.
   */
  public static boolean isInteger(String value) {
    try {
      Integer.parseInt(value);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
  
  /**
   * Добавление строки в конец основной (если она не содержит добавляемую).
   * 
   * @param baseLine      основная строка
   * @param newLine      добавляемая строка
   * @return строка с прибавлением новой
   */
  public final static String appendStrIfNotPresent(String baseLine, String newLine){
    if (JepRiaUtil.isEmpty(baseLine)) baseLine = "";
    int idx = indexOfLine(baseLine, newLine);

    // Only add the style if it's not already present.
    if (idx == -1) {
      if (baseLine.length() > 0) {
        return baseLine + " " + newLine;
      } 
      else {
        return newLine;
      }
    }
    return baseLine; 
  }
  
  /**
   * Определение индекса вхождения строки в строке
   * 
   * @param nameList      основная строка
   * @param name        проверяемая на вхождение строка
   * @return индекс вхождения строки
   */
  public final static int indexOfLine(String nameList, String name) {
    int idx = nameList.indexOf(name);

    // Calculate matching index.
    while (idx != -1) {
      if (idx == 0 || nameList.charAt(idx - 1) == ' ') {
        int last = idx + name.length();
        int lastPos = nameList.length();
        if ((last == lastPos)
            || ((last < lastPos) && (nameList.charAt(last) == ' '))) {
          break;
        }
      }
      idx = nameList.indexOf(name, idx + 1);
    }

    return idx;
  }
  
  /**
   * Удаление строки из основной (если она содержит удаляемую).
   * 
   * @param baseLine основная строка
   * @param removedLine удаляемая строка
   * @return новая строка
   */
  public final static String removeStrIfPresent(String baseLine, String removedLine) {
    if (JepRiaUtil.isEmpty(baseLine)) {
      return baseLine;
    }
    int idx = indexOfLine(baseLine, removedLine);
    // Don't try to remove the style if it's not there.
    if (idx != -1) {
      // Get the leading and trailing parts, without the removed name.
      String begin = baseLine.substring(0, idx).trim();
      String end = baseLine.substring(idx + removedLine.length()).trim();

      // Some contortions to make sure we don't leave extra spaces.
      String newClassName;
      if (begin.length() == 0) {
        newClassName = end;
      } else if (end.length() == 0) {
        newClassName = begin;
      } else {
        newClassName = begin + " " + end;
      }
      return newClassName;
    }
    return baseLine;
  }
}
