package com.technology.jep.jepria.server.service.rest;

import java.util.Comparator;
import java.util.Map;

import com.technology.jep.jepria.shared.util.DefaultComparator;

/**
 * Сортировщик списка записей по определённому полю
 */
public class ListSorter implements Comparator<Map<String, Object>> {

  /**
   * Поле для сортировки записей
   */
  protected final String sortFieldName;
  
  /**
   * Порядок сортировки: >= 0: в натуральном порядке, < 0: в обратном порядке
   */
  protected final int sortOrder;
  
  public ListSorter(String sortFieldName) {
    this(sortFieldName, 1);
  }
  
  public ListSorter(String sortFieldName, int sortOrder) {
    this(sortFieldName, sortOrder, DefaultComparator.instance);
  }
  
  /**
   * Схема сравнения значений полей записей
   */
  protected final Comparator<Object> valueComparator;

  /**
   * 
   * @param sortFieldName Поле для сортировки записей
   * @param valueComparator Схема сравнения значений полей записей
   */
  public ListSorter(String sortFieldName, int sortOrder, Comparator<Object> valueComparator) {
    this.sortFieldName = sortFieldName;
    this.sortOrder = sortOrder;
    this.valueComparator = valueComparator;
  }

  @Override
  public int compare(Map<String, Object> m1, Map<String, Object> m2) {
    if (sortFieldName != null) {
      Object v1 = m1.get(sortFieldName);
      Object v2 = m2.get(sortFieldName);
      return valueComparator.compare(sortOrder >= 0 ? v1 : v2, sortOrder >= 0 ? v2 : v1);
    }
    return valueComparator.compare(sortOrder >= 0 ? m1 : m2, sortOrder >= 0 ? m2 : m1);
  }
}
