package com.technology.jep.jepria.shared.dto;

import java.util.Comparator;

import com.technology.jep.jepria.shared.util.DefaultComparator;

/**
 * Сортирует набор данных.
 * 
 * @param <M> тип данных
 */
public class JepSorter<M extends JepDto> {

  /**
   * Схема сравнения.
   */
  protected Comparator<Object> comparator;

  /**
   * Создает новый сортировщик со схемой сравнения по умолчанию.
   */
  public JepSorter() {
    this.comparator = DefaultComparator.instance;
  }

  /**
   * Создает новый сортировщик с заданной схемой сравнения.
   * 
   * @param comparator схема сравнения
   */
  public JepSorter(Comparator<Object> comparator) {
    this.comparator = comparator;
  }

  /**
   * Сравнивает значение поля из двух записей.
   *
   * @param m1 запись для сравнения первая
   * @param m2 запись для сравнения вторая
   * @param property поле сравнения
   * @return результат сравнения: 0 - значения равны или оба значения равны null, -1 - если первое значение меньше второго, 1 - если первое значение больше второго
   */
  public int compare(M m1, M m2, String property) {
    if (property != null) {
      Object v1 = m1.get(property);
      Object v2 = m2.get(property);
      return comparator.compare(v1, v2);
    }
    return comparator.compare(m1, m2);
  }
}
