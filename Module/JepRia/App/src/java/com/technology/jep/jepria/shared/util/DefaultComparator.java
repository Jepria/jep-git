package com.technology.jep.jepria.shared.util;

import java.util.Comparator;

/**
 * Реализует схему сравнения по умолчанию.
 */
public class DefaultComparator<X extends Object> implements Comparator<X> {

  /**
   * Экземпляр схемы сравнения по умолчанию (singleton).
   */
  public final static DefaultComparator<Object> instance = new DefaultComparator<Object>();

  /**
   * Метод сравнения двух объектов.
   *
   * @param o1 объект сравнения первый
   * @param o2 объект сравнения второй
   * @return результат сравнения: 0 - объекты равны или оба объекта равны null, -1 - если первый объект меньше второго, 1 - если первый объект больше второго
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public int compare(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
      if (o1 == null && o2 == null) {
        return 0;
      } else {
        return (o1 == null) ? 1 : -1;
      }
    }
    if (o1 instanceof Comparable) {
      return ((Comparable) o1).compareTo(o2);
    }
    return compareStrings(o1.toString(), o2.toString());

  }

  /**
   * Метод сравнения двух строк с приведением их единому регистру.
   *
   * @param s1 строка сравнения первая
   * @param s2 строка сравнения вторая
   * @return результат сравнения: 0 - строки равны или обе строки равны null, -1 - если первая строка меньше второй, 1 - если первая строка больше второй
   */
  protected int compareStrings(String s1, String s2) {
    return s1.toLowerCase().compareTo(s2.toLowerCase());
  }

}
