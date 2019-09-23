package org.jepria.server.data;

/**
 * Dto для представления конфигурации сортировки списка по конкретному столбцу
 */
public class ColumnSortConfigurationDto {
  
  public ColumnSortConfigurationDto() {}
  
  private String columnName;
  
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

