package org.jepria.server.load.rest;

/**
 * Конфигурация сортировки значений определенного поля списка
 */
public class FieldSortConfigDto {
  
  /**
   * Имя поля
   */
  private String fieldName;
  
  /**
   * Порядок сортировки
   */
  private String sortOrder;

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(String sortOrder) {
    this.sortOrder = sortOrder;
  }
  
  
}
