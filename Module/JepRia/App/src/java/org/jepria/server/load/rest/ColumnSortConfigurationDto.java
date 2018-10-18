package org.jepria.server.load.rest;

/**
 * Конфигурация сортировки значений определенного поля списка
 */
public class ColumnSortConfigurationDto {
  
  /**
   * Имя поля
   */
  private String columnName;
  
  /**
   * Порядок сортировки
   */
  private String sortOrder;

  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public String getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(String sortOrder) {
    this.sortOrder = sortOrder;
  }
  
  
}
